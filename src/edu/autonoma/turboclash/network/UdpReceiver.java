package edu.autonoma.turboclash.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceiver {

    private DatagramSocket socket;
    private boolean activo;

    public UdpReceiver(DatagramSocket socket) {
        this.socket = socket;
        this.activo = true;
    }

    public void escuchar() {
        new Thread(() -> {
            try {
                while (activo) {
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String data = new String(packet.getData(), 0, packet.getLength());

                    GameMessage message = GameMessage.deserialize(data);

                    System.out.println("Recibido: " + message.type + " de " + message.playerName);



                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void detener() {
        activo = false;
    }
}
