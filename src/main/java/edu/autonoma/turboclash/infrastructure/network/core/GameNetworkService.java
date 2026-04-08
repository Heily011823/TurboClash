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
        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);
        peer.enviarATodos(msg);
    }

    /**
     * Ejecuta la operacion {@code sendMovement}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendMovement(Player player) {
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
        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT);
        peer.enviarATodos(msg);
    }

    /**
     * Ejecuta la operacion {@code send}.
     *
     */

    public void discover() {
        GameMessage msg = messageFactory.createDiscovery();


        for (int port : networkConfig.getPorts()) {
            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void join(GameContext context, Player player) {

        sendJoin(player);


        context.addPlayer(player);
    }
}
