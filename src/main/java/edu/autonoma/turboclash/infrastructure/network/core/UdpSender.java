package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Representa la responsabilidad de {@code UdpSender} en la infraestructura de red.
 */
public class UdpSender implements IMessageSender {

    private final DatagramSocket socket;

    /**
     * Crea una nueva instancia de {@code UdpSender}.
     *
     * @param socket valor del parametro {@code socket}
     */
    public UdpSender(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
    }

    @Override
    /**
     * Envia {@code Mensaje}.
     *
     * @param message valor del parametro {@code message}
     * @param ipDestino valor del parametro {@code ipDestino}
     * @param puertoDestino valor del parametro {@code puertoDestino}
     */
    public void enviarMensaje(GameMessage message, String ipDestino, int puertoDestino) {

        if (message == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }

        if (ipDestino == null || ipDestino.isEmpty()) {
            throw new IllegalArgumentException("IP destino inválida");
        }

        try {
            byte[] buffer = serialize(message);
            InetAddress address = InetAddress.getByName(ipDestino);

            DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    address,
                    puertoDestino
            );

            socket.send(packet);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando mensaje UDP", e);
        }
    }

    /**
     * Ejecuta la operacion {@code serialize}.
     *
     * @param message valor del parametro {@code message}
     * @return resultado de la operacion {@code serialize}
     */
    private byte[] serialize(GameMessage message) {
        return message.serialize().getBytes();
    }
}
