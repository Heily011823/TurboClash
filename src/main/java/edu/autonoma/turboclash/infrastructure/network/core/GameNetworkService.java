package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.application.GameContext;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;

/**
 * Representa la responsabilidad de {@code GameNetworkService} en la infraestructura de red.
 */
public class GameNetworkService {

    private final UdpPeer peer;
    private final GameMessageFactory messageFactory;
    private final NetworkConfig networkConfig;
    private Thread connectionThread;

    /**
     * Crea una nueva instancia de {@code GameNetworkService}.
     *
     * @param peer valor del parametro {@code peer}
     * @param messageFactory valor del parametro {@code messageFactory}
     */

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

    /**
     * Ejecuta la operacion {@code sendJoin}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendJoin(Player player) {
        if (player == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);
        peer.enviarATodos(msg);
    }

    /**
     * Ejecuta la operacion {@code sendMovement}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendMovement(Player player) {
        if (player == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints());
        peer.enviarATodos(msg);
    }

    /**
     * Ejecuta la operacion {@code sendLeave}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendLeave(Player player) {
        if (player == null || !peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT);
        peer.enviarATodos(msg);
    }

    /**
     * Ejecuta la operacion {@code send}.
     *
     */

    public void discover() {
        if (!peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.createDiscovery();

        for (int port : networkConfig.getPorts()) {
            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void discover(String hostIp, int hostPort) {
        if (!peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.createDiscovery();
        peer.getSender().enviarMensaje(msg, hostIp, hostPort);
    }

    public void join(GameContext context, Player player) {
        if (!peer.isActivo()) {
            return;
        }

        sendJoin(player);
        context.addPlayer(player);
    }

    public void connect(GameContext context, Player player, String hostIp, int hostPort) {
        if (connectionThread != null && connectionThread.isAlive()) {
            return;
        }

        connectionThread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                if (!peer.isActivo()) {
                    break;
                }

                if (!context.getMatch().getRemotePlayers().isEmpty()) {
                    break;
                }

                try {
                    peer.agregarPeer(hostIp, hostPort);
                    discover(hostIp, hostPort);
                    sendJoin(player);
                    Thread.sleep(500);
                } catch (RuntimeException e) {
                    if (!peer.isActivo()) {
                        break;
                    }
                    throw e;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        connectionThread.setName("Network-Connect-Thread");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }
}
