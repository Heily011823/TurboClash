package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UdpSender implements IMessageSender {

    private final DatagramSocket socket;

    public UdpSender(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
    }

    @Override
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

    private byte[] serialize(GameMessage message) {
        return message.serialize().getBytes();
    }
}