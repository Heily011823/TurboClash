package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.network.message.GameMessage;
import edu.autonoma.turboclash.network.message.MessageType;

import java.net.DatagramSocket;
import java.util.List;

public class NetworkFactory {

    private final NetworkConfig networkConfig;

    public NetworkFactory(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    public UdpPeer createPeer(int puertoLocal,
                              Player localPlayer,
                              List<Player> remotePlayers,
                              GameMessageHandler handler,
                              GameMessageFactory messageFactory) {

        try {
            DatagramSocket socket = new DatagramSocket(puertoLocal);

            // 🔥 habilitar broadcast
            socket.setBroadcast(true);

            IMessageSender sender = new UdpSender(socket);
            IMessageReceiver receiver = new UdpReceiver(socket);

            UdpPeer peer = new UdpPeer(socket, sender, receiver);


            receiver.setListener((msg, ip, port) -> {


                if (port != puertoLocal) {
                    peer.agregarPeer(ip, port);
                }


                handler.handle(msg);


                if (msg.getType() == MessageType.PLAYER_JOINED) {

                    GameMessage response = messageFactory.create(
                            localPlayer,
                            MessageType.PLAYER_JOINED
                    );

                    sender.enviarMensaje(response, ip, port);
                }
            });

            peer.iniciar();
            return peer;

        } catch (Exception e) {
            throw new RuntimeException("Error red", e);
        }
    }
}