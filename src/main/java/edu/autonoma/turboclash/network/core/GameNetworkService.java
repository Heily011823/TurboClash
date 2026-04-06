package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.message.GameMessage;
import edu.autonoma.turboclash.network.message.MessageType;
import edu.autonoma.turboclash.network.factory.GameMessageFactory;

public class GameNetworkService {

    private final UdpPeer peer;
    private final GameMessageFactory messageFactory;

    public GameNetworkService(UdpPeer peer, GameMessageFactory messageFactory) {
        this.peer = peer;
        this.messageFactory = messageFactory;
    }

    public void sendJoin(Player player) {
        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED);
        send(msg);
    }

    public void sendMovement(Player player) {
        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT);
        msg.setScore(player.getCurrentPoints()); // ✔ encapsulado
        send(msg);
    }

    public void sendLeave(Player player) {
        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT);
        send(msg);
    }

    private void send(GameMessage msg) {
        try {
            peer.enviarATodos(msg);
        } catch (Exception e) {
            System.err.println("Error enviando mensaje: " + e.getMessage());
        }
    }
}