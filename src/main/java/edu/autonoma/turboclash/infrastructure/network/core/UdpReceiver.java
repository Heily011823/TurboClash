package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Representa la responsabilidad de {@code UdpReceiver} en la infraestructura de red.
 */
public class UdpReceiver implements IMessageReceiver {

    private final DatagramSocket socket;
    private volatile boolean activo;
    private IMessageListener listener;

    /**
     * Crea una nueva instancia de {@code UdpReceiver}.
     *
     * @param socket valor del parametro {@code socket}
     */
    public UdpReceiver(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
        this.activo = true;
    }

    @Override
    /**
     * Actualiza el valor de {@code Listener}.
     *
     * @param listener valor del parametro {@code listener}
     */
    public void setListener(IMessageListener listener) {
        this.listener = listener;
    }

    @Override
    /**
     * Inicia la escucha de la operacion principal del metodo.
     */
    public void escuchar() {
        try {
            while (activo && !socket.isClosed()) {

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                String data = new String(packet.getData(), 0, packet.getLength());

                try {
                    GameMessage message = GameMessage.deserialize(data);

                    InetAddress ipAddress = packet.getAddress();
                    String ip = ipAddress.getHostAddress();
                    int puerto = packet.getPort();

                    System.out.println(
                            "Recibido: " + message.getType() +
                                    " de " + message.getPlayerName()
                    );

                    if (listener != null) {
                        listener.onMessage(message, ip, puerto);
                    }

                } catch (Exception e) {
                    System.err.println("Mensaje inválido: " + data);
                }
            }
        } catch (Exception e) {
            if (activo) {
                throw new RuntimeException("Error en recepción UDP", e);
            }
        }
    }

    @Override
    /**
     * Detiene la operacion principal del metodo.
     */
    public void detener() {
        activo = false;

        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
