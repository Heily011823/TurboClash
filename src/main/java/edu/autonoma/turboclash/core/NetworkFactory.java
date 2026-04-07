package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;

import java.net.DatagramSocket;
import java.util.List;

public class NetworkFactory {
    public UdpPeer createPeer(int puertoLocal, List<Player> remotePlayers) {
        try {
            DatagramSocket socket = new DatagramSocket(puertoLocal);

            IMessageSender sender = new UdpSender(socket);
            IMessageReceiver receiver = new UdpReceiver(socket);

            UdpPeer peer = new UdpPeer(socket, sender, receiver);

            NetworkConfig.getPeers().forEach((port, ip) -> {
                if (port != puertoLocal) {
                    peer.agregarPeer(ip, port);
                }
            });

            GameMessageHandler handler = new GameMessageHandler(remotePlayers);
            receiver.setListener((msg, ip, port) -> handler.handle(msg));

            peer.iniciar();
            return peer;

        } catch (Exception e) {
            throw new RuntimeException("Error red", e);
        }
    }
}
