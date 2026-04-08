package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.infrastructure.network.core.*;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramSocket;

/**
 * Crea y configura instancias relacionadas con {@code NetworkFactory} en la infraestructura del sistema.
 */
public class NetworkFactory {

    private final NetworkConfig networkConfig;

    /**
     * Crea una nueva instancia de {@code NetworkFactory}.
     *
     * @param networkConfig valor del parametro {@code networkConfig}
     */
    public NetworkFactory(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Crea {@code Peer}.
     *
     * @param puerto valor del parametro {@code puerto}
     * @param localPlayer valor del parametro {@code localPlayer}
     * @param match valor del parametro {@code match}
     * @param handler valor del parametro {@code handler}
     * @param factory valor del parametro {@code factory}
     * @return instancia creada para {@code Peer}
     */
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
