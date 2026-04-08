package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.application.GameContext;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

/**
 * Servicio de red del juego para operaciones P2P.
 */
public class GameNetworkService {

    private final UdpPeer peer;
    private final GameMessageFactory messageFactory;
    private final NetworkConfig networkConfig;

    private volatile boolean connecting = false;

    /**
     * Numero esperado de remotos.
     * Si juegan 4 en total, cada cliente debe ver 3 remotos.
     */
    private static final int EXPECTED_REMOTE_PLAYERS = 3;

    public GameNetworkService(UdpPeer peer,
                              GameMessageFactory messageFactory,
                              NetworkConfig networkConfig) {
        this.peer = peer;
        this.messageFactory = messageFactory;
        this.networkConfig = networkConfig;
    }

    public UdpPeer getPeer() {
        return peer;
    }

    public void sendJoin(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);
        peer.enviarATodos(msg);
    }

    public void sendMovement(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints());
        peer.enviarATodos(msg);
    }

    public void sendLeave(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT);
        peer.enviarATodos(msg);
    }

    public void discover() {
        if (!peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.createDiscovery();

        for (int port : networkConfig.getPorts()) {
            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                System.err.println("Error enviando discovery al puerto " + port + ": " + e.getMessage());
            }
        }
    }

    /**
     * Envía join y agrega el jugador local al contexto.
     */
    public void join(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo()) {
            return;
        }

        context.addPlayer(player);
        sendJoin(player);
    }

    /**
     * Inicia conexión P2P.
     *
     * Corregido:
     * - ya no se detiene cuando aparece el primer remoto
     * - sigue intentando hasta completar los remotos esperados
     *   o hasta agotar intentos
     */
    public void connect(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo() || connecting) {
            return;
        }

        connecting = true;

        Thread connectionThread = new Thread(() -> {
            try {
                int maxAttempts = 10;

                for (int i = 0; i < maxAttempts; i++) {
                    if (!peer.isActivo()) {
                        break;
                    }

                    int remoteCount = context.getMatch().getRemotePlayers().size();

                    System.out.println("Intento de conexión " + (i + 1)
                            + " | remotos detectados: " + remoteCount);

                    if (remoteCount >= EXPECTED_REMOTE_PLAYERS) {
                        System.out.println("Conexión completa. Remotos detectados: " + remoteCount);
                        break;
                    }

                    discover();
                    sendJoin(player);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                System.out.println("Jugadores remotos conectados: "
                        + context.getMatch().getRemotePlayers().size());

            } catch (Exception e) {
                if (peer.isActivo()) {
                    System.err.println("Error durante connect(): " + e.getMessage());
                }
            } finally {
                connecting = false;
            }
        });

        connectionThread.setName("Network-Connect-Thread");
        connectionThread.setDaemon(false);
        connectionThread.start();
    }
}