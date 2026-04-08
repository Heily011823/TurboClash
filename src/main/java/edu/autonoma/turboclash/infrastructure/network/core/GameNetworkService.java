package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;

public class GameNetworkService {

    private final UdpPeer peer;
    private final GameMessageFactory messageFactory;

    public GameNetworkService(UdpPeer peer, GameMessageFactory messageFactory) {
        this.peer = peer;
        this.messageFactory = messageFactory;
    }

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

    public void sendMovement(Player player) {
        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints());
        send(msg);
    }

    public void sendLeave(Player player) {
        send(messageFactory.create(player, MessageType.PLAYER_LEFT));
    }

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