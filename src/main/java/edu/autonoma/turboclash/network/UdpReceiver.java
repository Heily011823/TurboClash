package edu.autonoma.turboclash.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceiver {

    private DatagramSocket socket;
    private boolean activo;


    public interface MessageListener {
        void onMessage(GameMessage message, InetAddress ip, int puerto);
    }

    private MessageListener listener;

    public UdpReceiver(DatagramSocket socket) {
        this.socket = socket;
        this.activo = true;
    }

    // Permite conectar con la lógica del juego
    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public void escuchar() {
        try {
            while (activo) {

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String data = new String(packet.getData(), 0, packet.getLength());

                try {
                    GameMessage message = GameMessage.deserialize(data);

                    InetAddress ip = packet.getAddress();
                    int puerto = packet.getPort();

                    System.out.println("Recibido: " + message.type + " de " + message.playerName);


                    if (listener != null) {
                        listener.onMessage(message, ip, puerto);
                    }

                } catch (Exception e) {
                    System.out.println("Mensaje inválido recibido: " + data);
                }
            }
        } catch (Exception e) {
            if (activo) {
                e.printStackTrace();
            }
        }
    }

    public void detener() {
        activo = false;
    }
}