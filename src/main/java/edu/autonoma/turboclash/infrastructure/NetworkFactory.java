package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.infrastructure.network.core.*;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramSocket;

public class NetworkFactory {

    private final NetworkConfig networkConfig;

    public NetworkFactory(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public UdpPeer createPeer(
            int puerto,
            Player localPlayer,
            Match match,
            GameMessageHandler handler,
            GameMessageFactory factory
    ) {

        try {
            DatagramSocket socket = new DatagramSocket(puerto);

            socket.setBroadcast(true);

            IMessageSender sender = new UdpSender(socket);
            IMessageReceiver receiver = new UdpReceiver(socket);

            UdpPeer peer = new UdpPeer(socket, sender, receiver);

            receiver.setListener((msg, ip, port) -> {


                if (port != puerto) {
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