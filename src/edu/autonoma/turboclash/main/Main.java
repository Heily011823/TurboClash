package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.network.UdpPeer;

public class Main {
    public static void main(String[] args) {

        UdpPeer peer = new UdpPeer(5000);
        peer.startListening();

        // Prueba local
        peer.sendMessage("Hola UDP", "127.0.0.1", 5000);
    }
}
