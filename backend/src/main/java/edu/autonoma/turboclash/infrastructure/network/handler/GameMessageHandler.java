package edu.autonoma.turboclash.infrastructure.network.handler;

import edu.autonoma.turboclash.application.AuthoritativeMatchCoordinator;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.strategy.GameOverStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.GameStartStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.IMessageStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.JoinStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.LeaveStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.MoveStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.ScoreStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.SyncStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestiona la recepción y el enrutamiento de mensajes UDP del juego.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Filtrar mensajes inválidos o duplicados.</li>
 *   <li>Evitar procesar mensajes propios.</li>
 *   <li>Delegar el manejo de cada tipo de mensaje a una estrategia.</li>
 *   <li>Garantizar que mensajes autoritativos como {@code SYNC},
 *       {@code GAME_START} y {@code GAME_OVER} solo se acepten
 *       desde el host autoritativo.</li>
 * </ul>
 *
 * <p><strong>Mejora aplicada:</strong>
 * se agregan logs detallados para depurar por qué un cliente puede estar
 * ignorando mensajes críticos del host, especialmente {@code GAME_OVER}.
 * Esto ayuda a detectar inconsistencias en la elección del host autoritativo.</p>
 *
 * @author Valerie Moreno Castaño
 * @version 1.1
 * @since 2025-04-09
 */
public class GameMessageHandler {

    private final Map<MessageType, IMessageStrategy> strategies = new HashMap<>();
    private final Match match;
    private UdpPeer peer;
    private GameMessageFactory messageFactory;
    private AuthoritativeMatchCoordinator coordinator;
    private final Set<String> processedJoins = ConcurrentHashMap.newKeySet();

    /**
     * Crea un nuevo manejador de mensajes del juego.
     *
     * @param match estado compartido de la partida
     * @param coordinator coordinador del flujo autoritativo del match
     */
    public GameMessageHandler(Match match, AuthoritativeMatchCoordinator coordinator) {
        this.match = match;
        this.coordinator = coordinator;
        registerStrategies();
    }

    /**
     * Procesa un mensaje UDP recibido.
     *
     * <p>Flujo general:</p>
     * <ol>
     *   <li>Valida nulos.</li>
     *   <li>Registra el puerto del remitente.</li>
     *   <li>Ignora mensajes propios.</li>
     *   <li>Descarta duplicados o paquetes viejos.</li>
     *   <li>Verifica si el mensaje debe venir del host autoritativo.</li>
     *   <li>Delega el procesamiento a la estrategia correspondiente.</li>
     * </ol>
     *
     * @param msg mensaje recibido
     * @param ip IP del remitente
     * @param port puerto físico del remitente
     */
    public void handle(GameMessage msg, String ip, int port) {
        if (msg == null || match == null) {
            System.out.println("[WARN][GameMessageHandler] Mensaje o match nulo. Se ignora.");
            return;
        }

        System.out.println("[DEBUG][GameMessageHandler] Mensaje UDP recibido "
                + "type=" + msg.getType()
                + " from=" + ip + ":" + port
                + " advertisedPort=" + msg.getPort()
                + " playerId=" + msg.getPlayerId()
                + " playerName=" + msg.getPlayerName()
                + " sequence=" + msg.getSequence());

        /*
         * Registra el puerto anunciado por el mensaje para ayudar
         * a que todos los nodos converjan al mismo host autoritativo.
         */
        match.registerKnownPort(msg.getPort());

        /*
         * Ignora mensajes emitidos por la propia instancia local,
         * evitando reprocesamiento de broadcast/reenvíos.
         */
        if (peer != null && msg.getPort() == peer.getLocalPort()) {
            System.out.println("[DEBUG][GameMessageHandler] Mensaje ignorado: proviene del puerto local "
                    + peer.getLocalPort());
            return;
        }

        Player localPlayer = match.getLocalPlayer();
        if (isLocalMessage(msg, localPlayer)) {
            System.out.println("[DEBUG][GameMessageHandler] Mensaje ignorado: pertenece al jugador local.");
            return;
        }

        /*
         * DISCOVERY se trata aparte porque inicia el intercambio
         * de HANDSHAKE y PLAYER_JOINED.
         */
        if (msg.getType() == MessageType.DISCOVERY) {
            handleDiscovery(ip, port, localPlayer);
            return;
        }

        /*
         * Evita procesar múltiples joins idénticos.
         */
        if ((msg.getType() == MessageType.HANDSHAKE || msg.getType() == MessageType.PLAYER_JOINED)
                && !processedJoins.add(buildJoinKey(msg))) {
            System.out.println("[DEBUG][GameMessageHandler] Join duplicado ignorado: " + buildJoinKey(msg));
            return;
        }

        /*
         * Evita reprocesar movimientos o puntajes con secuencias antiguas.
         */
        if ((msg.getType() == MessageType.MOVEMENT || msg.getType() == MessageType.SCORE_UPDATE)
                && !match.isPacketFresh(msg.getPlayerId(), msg.getPlayerName(), msg.getSequence())) {
            System.out.println("[DEBUG][GameMessageHandler] Paquete viejo o repetido ignorado. type="
                    + msg.getType() + " sequence=" + msg.getSequence());
            return;
        }

        /*
         * Solo el host autoritativo debe procesar movimiento como base
         * del estado compartido del match.
         */
        if (msg.getType() == MessageType.MOVEMENT && !shouldProcessMovement(msg)) {
            System.out.println("[DEBUG][GameMessageHandler] MOVEMENT ignorado: esta instancia no es host. "
                    + "localPort=" + getLocalPortSafe()
                    + " authoritativeHost=" + match.getAuthoritativeHostPort());
            return;
        }

        /*
         * Mensajes críticos del estado global solo deben aceptarse
         * desde el host autoritativo.
         */
        if (isAuthoritativeMessage(msg.getType())) {
            int expectedHost = match.getAuthoritativeHostPort();
            int incomingPort = msg.getPort();

            if (incomingPort != expectedHost) {
                System.out.println("[WARN][GameMessageHandler] Mensaje autoritativo ignorado. "
                        + "type=" + msg.getType()
                        + " incomingPort=" + incomingPort
                        + " expectedHost=" + expectedHost
                        + " localPort=" + getLocalPortSafe()
                        + " reason=El remitente no coincide con el host autoritativo actual.");
                return;
            }

            System.out.println("[DEBUG][GameMessageHandler] Mensaje autoritativo aceptado. "
                    + "type=" + msg.getType()
                    + " host=" + expectedHost);
        }

        IMessageStrategy strategy = strategies.get(msg.getType());

        System.out.println("[DEBUG][GameMessageHandler] Decisión final "
                + "MessageType=" + msg.getType()
                + " strategy=" + (strategy != null ? strategy.getClass().getSimpleName() : "sin estrategia"));

        if (strategy != null) {
            strategy.handle(msg);
        } else {
            System.out.println("[WARN][GameMessageHandler] No existe estrategia para el tipo: " + msg.getType());
        }
    }

    /**
     * Registra las estrategias por tipo de mensaje.
     */
    private void registerStrategies() {
        strategies.put(MessageType.HANDSHAKE, new JoinStrategy(match));
        strategies.put(MessageType.PLAYER_JOINED, new JoinStrategy(match));
        strategies.put(MessageType.MOVEMENT, new MoveStrategy(match));
        strategies.put(MessageType.SCORE_UPDATE, new ScoreStrategy(match));
        strategies.put(MessageType.PLAYER_LEFT, new LeaveStrategy(match));
        strategies.put(MessageType.GAME_START, new GameStartStrategy(coordinator));
        strategies.put(MessageType.SYNC, new SyncStrategy(coordinator));
        strategies.put(MessageType.GAME_OVER, new GameOverStrategy(coordinator));
    }

    /**
     * Maneja el descubrimiento de peers.
     *
     * <p>Cuando una instancia recibe {@code DISCOVERY}, agrega el peer
     * y responde con {@code HANDSHAKE} y {@code PLAYER_JOINED}.</p>
     *
     * @param ip IP del peer remoto
     * @param port puerto del peer remoto
     * @param localPlayer jugador local
     */
    private void handleDiscovery(String ip, int port, Player localPlayer) {
        if (peer != null) {
            peer.agregarPeer(ip, port);
        }

        if (peer != null && messageFactory != null && localPlayer != null) {
            GameMessage handshake = messageFactory.create(
                    localPlayer,
                    MessageType.HANDSHAKE,
                    peer.getLocalPort(),
                    System.currentTimeMillis(),
                    false
            );
            peer.getSender().enviarMensaje(handshake, ip, port);

            GameMessage join = messageFactory.create(
                    localPlayer,
                    MessageType.PLAYER_JOINED,
                    peer.getLocalPort(),
                    System.currentTimeMillis(),
                    false
            );
            peer.getSender().enviarMensaje(join, ip, port);

            System.out.println("[DEBUG][GameMessageHandler] Respuesta a DISCOVERY enviada a "
                    + ip + ":" + port + " con HANDSHAKE y PLAYER_JOINED.");
        }
    }

    /**
     * Indica si esta instancia debe procesar mensajes de movimiento.
     *
     * <p>Solo el host autoritativo debe tomar movimientos como base
     * del estado oficial del match.</p>
     *
     * @param msg mensaje recibido
     * @return true si el movimiento debe procesarse
     */
    private boolean shouldProcessMovement(GameMessage msg) {
        if (peer == null) {
            return true;
        }

        int localPort = peer.getLocalPort();
        int hostPort = match.getAuthoritativeHostPort();

        boolean shouldProcess = hostPort == localPort;

        System.out.println("[DEBUG][GameMessageHandler] Validación MOVEMENT "
                + "fromPort=" + msg.getPort()
                + " localPort=" + localPort
                + " authoritativeHost=" + hostPort
                + " process=" + shouldProcess);

        return shouldProcess;
    }

    /**
     * Determina si un mensaje corresponde al jugador local.
     *
     * @param msg mensaje recibido
     * @param localPlayer jugador local
     * @return true si el mensaje pertenece al jugador local
     */
    private boolean isLocalMessage(GameMessage msg, Player localPlayer) {
        if (localPlayer == null) {
            return false;
        }

        boolean sameAsLocalById = localPlayer.getId() != null
                && msg.getPlayerId() != null
                && localPlayer.getId().equals(msg.getPlayerId());

        boolean sameAsLocalByName = localPlayer.getName() != null
                && msg.getPlayerName() != null
                && localPlayer.getName().equalsIgnoreCase(msg.getPlayerName());

        return sameAsLocalById || sameAsLocalByName;
    }

    /**
     * Construye una clave para detectar joins duplicados.
     *
     * @param msg mensaje de join
     * @return clave compuesta
     */
    private String buildJoinKey(GameMessage msg) {
        String playerId = msg.getPlayerId() != null ? msg.getPlayerId() : "";
        String playerName = msg.getPlayerName() != null ? msg.getPlayerName() : "";
        return msg.getType() + "|" + playerId + "|" + playerName + "|" + msg.getPort();
    }

    /**
     * Determina si el tipo de mensaje debe provenir del host autoritativo.
     *
     * @param type tipo de mensaje
     * @return true si es un mensaje autoritativo
     */
    private boolean isAuthoritativeMessage(MessageType type) {
        return type == MessageType.SYNC
                || type == MessageType.GAME_START
                || type == MessageType.GAME_OVER;
    }

    /**
     * Retorna el puerto local si existe un peer configurado.
     *
     * @return puerto local o -1 si no está disponible
     */
    private int getLocalPortSafe() {
        return peer != null ? peer.getLocalPort() : -1;
    }

    /**
     * Asigna el peer UDP asociado.
     *
     * @param peer peer local
     */
    public void setPeer(UdpPeer peer) {
        this.peer = peer;
    }

    /**
     * Asigna la fábrica de mensajes.
     *
     * @param messageFactory fábrica de mensajes
     */
    public void setMessageFactory(GameMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * Actualiza el coordinador y reconstruye las estrategias dependientes.
     *
     * @param coordinator nuevo coordinador
     */
    public void setCoordinator(AuthoritativeMatchCoordinator coordinator) {
        this.coordinator = coordinator;
        strategies.put(MessageType.GAME_START, new GameStartStrategy(coordinator));
        strategies.put(MessageType.SYNC, new SyncStrategy(coordinator));
        strategies.put(MessageType.GAME_OVER, new GameOverStrategy(coordinator));
    }
}