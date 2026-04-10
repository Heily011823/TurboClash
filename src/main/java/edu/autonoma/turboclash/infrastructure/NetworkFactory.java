package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.infrastructure.network.core.*;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramSocket;

/**
 * Representa la clase `NetworkFactory` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class NetworkFactory {

    private final NetworkConfig networkConfig;

    /**
     * Crea una nueva instancia de `NetworkFactory`.
     * @param networkConfig valor del parametro `networkConfig`
     */
    public NetworkFactory(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }

    /**
     * Crea el recurso necesario para create peer.
     * @param puerto valor del parametro `puerto`
     * @param localPlayer valor del parametro `localPlayer`
     * @param match valor del parametro `match`
     * @param handler valor del parametro `handler`
     * @param factory valor del parametro `factory`
     * @return resultado de la operacion documentada
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
                peer.agregarPeer(ip, port);
                handler.handle(msg, ip, port);
            });

            return peer;

        } catch (Exception e) {
            throw new RuntimeException("Error red", e);
        }
    }
}
