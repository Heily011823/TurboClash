package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceiver implements IMessageReceiver {

    private final DatagramSocket socket;
    private volatile boolean activo;


    public interface MessageListener {
        void onMessage(GameMessage message, InetAddress ip, int puerto);
    }

    private MessageListener listener;

    public UdpReceiver(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
        this.activo = true;
    }


    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void escuchar() {
        try {
            while (activo && !socket.isClosed()) {

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String data = new String(packet.getData(), 0, packet.getLength());

                try {
                    GameMessage message = GameMessage.deserialize(data);

                    InetAddress ip = packet.getAddress();
                    int puerto = packet.getPort();

                    System.out.println("Recibido: " + message.getType() + " de " + message.getPlayerName());


                    if (listener != null) {
                        listener.onMessage(message, ip, puerto);
                    }

                } catch (Exception e) {
                    System.out.println("Mensaje inválido recibido: " + data);
                }
            }
        } catch (Exception e) {
            if (activo) {
                System.err.println("Error en recepción UDP: " + e.getMessage());
            }
        }
    }

    @Override
    public void detener() {
        activo = false;

        if (!socket.isClosed()) {
            socket.close();
        }
    }
}