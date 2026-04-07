package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;

import java.net.DatagramSocket;
import java.util.List;

public class NetworkFactory {

    private final NetworkConfig networkConfig;

    public NetworkFactory(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public UdpPeer createPeer(int puertoLocal,
                              List<Player> remotePlayers,
                              GameMessageHandler handler) {

        try {
            DatagramSocket socket = new DatagramSocket(puertoLocal);


            socket.setBroadcast(true);

            IMessageSender sender = new UdpSender(socket);
            IMessageReceiver receiver = new UdpReceiver(socket);

            UdpPeer peer = new UdpPeer(socket, sender, receiver);


            receiver.setListener((msg, ip, port) -> {


                if (port != puertoLocal) {
                    peer.agregarPeer(ip, port);
                }


                handler.handle(msg);
            });

            peer.iniciar();
            return peer;

        } catch (Exception e) {
            throw new RuntimeException("Error red", e);
        }
    }
}