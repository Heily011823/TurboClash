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

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED, peer.getLocalPort());
        peer.enviarATodos(msg);
    }

    public void sendHandshake(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.HANDSHAKE, peer.getLocalPort());
        peer.enviarATodos(msg);
    }

    public void sendMovement(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT, peer.getLocalPort());
        msg.setScore(player.getCurrentPoints());
        peer.enviarATodos(msg);
    }

    public void sendLeave(Player player) {
        if (player == null || player.getCar() == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT, peer.getLocalPort());
        peer.enviarATodos(msg);
    }

    public void discover() {
        if (!peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.createDiscovery(peer.getLocalPort());

        for (int port : networkConfig.getPorts()) {
            if (port == peer.getLocalPort()) {
                continue;
            }

            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                System.err.println("Error enviando discovery al puerto " + port + ": " + e.getMessage());
            }
        }
    }

    public void join(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo()) {
            return;
        }

        sendJoin(player);
    }

    public void connect(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo() || connecting) {
            return;
        }

        connecting = true;

        Thread connectionThread = new Thread(() -> {
            try {
                int maxAttempts = 12;

                for (int i = 0; i < maxAttempts; i++) {
                    if (!peer.isActivo()) {
                        break;
                    }

                    int remoteCount = context.getMatch().getRemotePlayers().size();

                    System.out.println("Intento de conexión " + (i + 1)
                            + " | remotos detectados: " + remoteCount);

                    if (remoteCount >= EXPECTED_REMOTE_PLAYERS) {
                        break;
                    }

                    sendHandshake(player);
                    sendJoin(player);

                    if (i < 2) {
                        discover();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            } finally {
                connecting = false;
            }
        });

        connectionThread.setName("Network-Connect-Thread");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }
}