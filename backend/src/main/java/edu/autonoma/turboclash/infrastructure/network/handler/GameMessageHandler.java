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
 * Representa la clase `GameMessageHandler` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
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
     * Crea una nueva instancia de `GameMessageHandler`.
     * @param match valor del parametro `match`
     * @param coordinator valor del parametro `coordinator`
     */
    public GameMessageHandler(Match match, AuthoritativeMatchCoordinator coordinator) {
        this.match = match;
        this.coordinator = coordinator;
        registerStrategies();
    }

    /**
     * Ejecuta la operacion publica `handle`.
     * @param msg valor del parametro `msg`
     * @param ip valor del parametro `ip`
     * @param port valor del parametro `port`
     */
    public void handle(GameMessage msg, String ip, int port) {
        if (msg == null || match == null) {
            return;
        }

        System.out.println("[DEBUG][GameMessageHandler] Mensaje UDP recibido type=" + msg.getType()
                + " from=" + ip + ":" + port
                + " playerId=" + msg.getPlayerId()
                + " sequence=" + msg.getSequence());

        match.registerKnownPort(msg.getPort());

        if (peer != null && msg.getPort() == peer.getLocalPort()) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();
        if (isLocalMessage(msg, localPlayer)) {
            return;
        }

        if (msg.getType() == MessageType.DISCOVERY) {
            handleDiscovery(ip, port, localPlayer);
            return;
        }

        if ((msg.getType() == MessageType.HANDSHAKE || msg.getType() == MessageType.PLAYER_JOINED)
                && !processedJoins.add(buildJoinKey(msg))) {
            return;
        }

        if ((msg.getType() == MessageType.MOVEMENT || msg.getType() == MessageType.SCORE_UPDATE)
                && !match.isPacketFresh(msg.getPlayerId(), msg.getPlayerName(), msg.getSequence())) {
            return;
        }

        if (msg.getType() == MessageType.MOVEMENT && !shouldProcessMovement(msg)) {
            return;
        }

        if ((msg.getType() == MessageType.SYNC || msg.getType() == MessageType.GAME_START || msg.getType() == MessageType.GAME_OVER)
                && msg.getPort() != match.getAuthoritativeHostPort()) {
            return;
        }

        IMessageStrategy strategy = strategies.get(msg.getType());
        System.out.println("[DEBUG][GameMessageHandler] Decision MessageType=" + msg.getType()
                + " strategy=" + (strategy != null ? strategy.getClass().getSimpleName() : "sin estrategia"));
        if (strategy != null) {
            strategy.handle(msg);
        }
    }

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

    private void handleDiscovery(String ip, int port, Player localPlayer) {
        if (peer != null) {
            peer.agregarPeer(ip, port);
        }

        if (peer != null && messageFactory != null && localPlayer != null) {
            GameMessage handshake = messageFactory.create(localPlayer, MessageType.HANDSHAKE, peer.getLocalPort(),
                    System.currentTimeMillis(), false);
            peer.getSender().enviarMensaje(handshake, ip, port);

            GameMessage join = messageFactory.create(localPlayer, MessageType.PLAYER_JOINED, peer.getLocalPort(),
                    System.currentTimeMillis(), false);
            peer.getSender().enviarMensaje(join, ip, port);
        }
    }

    private boolean shouldProcessMovement(GameMessage msg) {
        if (peer == null) {
            return true;
        }

        int localPort = peer.getLocalPort();
        return match.getAuthoritativeHostPort() == localPort;
    }

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

    private String buildJoinKey(GameMessage msg) {
        String playerId = msg.getPlayerId() != null ? msg.getPlayerId() : "";
        String playerName = msg.getPlayerName() != null ? msg.getPlayerName() : "";
        return msg.getType() + "|" + playerId + "|" + playerName + "|" + msg.getPort();
    }

    /**
     * Actualiza el valor asociado a `setPeer`.
     * @param peer valor del parametro `peer`
     */
    public void setPeer(UdpPeer peer) {
        this.peer = peer;
    }

    /**
     * Actualiza el valor asociado a `setMessageFactory`.
     * @param messageFactory valor del parametro `messageFactory`
     */
    public void setMessageFactory(GameMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    /**
     * Actualiza el valor asociado a `setCoordinator`.
     * @param coordinator valor del parametro `coordinator`
     */
    public void setCoordinator(AuthoritativeMatchCoordinator coordinator) {
        this.coordinator = coordinator;
        strategies.put(MessageType.GAME_START, new GameStartStrategy(coordinator));
        strategies.put(MessageType.SYNC, new SyncStrategy(coordinator));
        strategies.put(MessageType.GAME_OVER, new GameOverStrategy(coordinator));
    }
}
