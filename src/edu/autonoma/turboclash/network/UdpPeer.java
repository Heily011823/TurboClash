package edu.autonoma.turboclash.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpPeer {

    private DatagramSocket socket;
    private int port;

    // Constructor
    public UdpPeer(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
            System.out.println("Escuchando en puerto: " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para recibir mensajes (en hilo)
    public void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());

                    System.out.println("Recibido: " + message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Método para enviar mensajes
    public void sendMessage(String message, String ip, int port) {
        try {
            byte[] buffer = message.getBytes();
            InetAddress address = InetAddress.getByName(ip);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}