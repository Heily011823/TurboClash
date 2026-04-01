package edu.autonoma.turboclash.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender {

    private DatagramSocket socket;

    public UdpSender(DatagramSocket socket) {
        this.socket = socket;
    }


    public void enviarMensaje(GameMessage message, String ipDestino, int puertoDestino) {
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
            System.out.println("Error enviando mensaje UDP");
            e.printStackTrace();
        }
    }
}