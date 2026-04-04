package edu.autonoma.turboclash.network;

import edu.autonoma.turboclash.model.Player;

public class GameNetworkService {

    private UdpPeer peer;

    public GameNetworkService(UdpPeer peer) {
        this.peer = peer;
    }

    public void sendJoin(Player player) {
        GameMessage msg = createBaseMessage(player, MessageType.PLAYER_JOINED);
        peer.enviarATodos(msg);
    }

    public void sendMovement(Player player) {
        GameMessage msg = createBaseMessage(player, MessageType.MOVEMENT);
        msg.score = player.getCurrentPoints();

        try {
            peer.enviarATodos(msg);
        } catch (Exception e) {
            System.err.println("Error enviando: " + e.getMessage());
        }
    }

    public void sendLeave(Player player) {
        GameMessage msg = createBaseMessage(player, MessageType.PLAYER_LEFT);
        peer.enviarATodos(msg);
    }

    private GameMessage createBaseMessage(Player player, MessageType type) {
        GameMessage msg = new GameMessage();
        msg.type = type;
        msg.playerId = player.getId();
        msg.playerName = player.getName();
        msg.posX = player.getCar().getX();
        msg.posY = player.getCar().getY();
        msg.time = System.currentTimeMillis();
        msg.event = "";
        return msg;
    }
}