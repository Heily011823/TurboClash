package edu.autonoma.turboclash.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    private DatagramSocket socket;
    private String ipDestino;
    private int puertoDestino;

    public UdpSender(DatagramSocket socket, String ipDestino, int puertoDestino) {
        this.socket = socket;
        this.ipDestino = ipDestino;
        this.puertoDestino = puertoDestino;
    }

    public void enviarMensaje(GameMessage message) {
        try {
            String data = message.serialize();
            byte[] buffer = data.getBytes();

            InetAddress address = InetAddress.getByName(ipDestino);

            DatagramPacket packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    address,
                    puertoDestino
            );

            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
