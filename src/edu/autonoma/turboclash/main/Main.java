package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.network.*;

public class Main {

    public static void main(String[] args) {

        UdpPeer peer = new UdpPeer("127.0.0.1", 5001, 5000);
        peer.iniciar();

        // Crear mensaje
        GameMessage msg = new GameMessage();
        msg.type = MessageType.MOVEMENT;
        msg.playerId = "1";
        msg.playerName = "Heily";
        msg.posX = 100;
        msg.posY = 200;
        msg.score = 10;
        msg.time = System.currentTimeMillis();
        msg.event = "none";

        peer.enviar(msg);
    }
}
