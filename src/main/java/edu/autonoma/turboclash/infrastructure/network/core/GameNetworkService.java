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
        if (player == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);
        peer.enviarATodos(msg);
    }

    public void sendMovement(Player player) {
        if (player == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints());
        peer.enviarATodos(msg);
    }

    public void sendLeave(Player player) {
        if (player == null || !peer.isActivo()) {
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
     * Inicia conexión P2P enviando varios DISCOVERY y JOIN.
     */
    public void connect(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo()) {
            return;
        }

        Thread connectionThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (!peer.isActivo()) {
                    break;
                }

                try {
                    discover();
                    sendJoin(player);
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    if (!peer.isActivo()) {
                        break;
                    }
                    System.err.println("Error durante connect(): " + e.getMessage());
                }
            }
        });

        connectionThread.setName("Network-Connect-Thread");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }
}