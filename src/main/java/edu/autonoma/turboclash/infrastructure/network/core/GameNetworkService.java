package edu.autonoma.turboclash.infrastructure.network.core;

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

    /**
     * Crea una nueva instancia de {@code GameNetworkService}.
     *
     * @param peer valor del parametro {@code peer}
     * @param messageFactory valor del parametro {@code messageFactory}
     */
    public GameNetworkService(UdpPeer peer, GameMessageFactory messageFactory) {
        this.peer = peer;
        this.messageFactory = messageFactory;
    }

    /**
     * Ejecuta la operacion {@code sendJoin}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendJoin(Player player) {

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);


        for (int port = 5000; port <= 5003; port++) {
            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ejecuta la operacion {@code sendMovement}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendMovement(Player player) {
        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints());
        send(msg);
    }

    /**
     * Ejecuta la operacion {@code sendLeave}.
     *
     * @param player valor del parametro {@code player}
     */
    public void sendLeave(Player player) {
        send(messageFactory.create(player, MessageType.PLAYER_LEFT));
    }

    /**
     * Ejecuta la operacion {@code send}.
     *
     * @param msg valor del parametro {@code msg}
     */
    private void send(GameMessage msg) {

        for (int port = 5000; port <= 5003; port++) {
            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
