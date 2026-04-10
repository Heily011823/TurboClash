package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * Representa la clase `UdpReceiver` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class UdpReceiver implements IMessageReceiver {

    private final DatagramSocket socket;
    private volatile boolean activo;
    private IMessageListener listener;

    /**
     * Crea una nueva instancia de `UdpReceiver`.
     * @param socket valor del parametro `socket`
     */
    public UdpReceiver(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
        this.activo = true;

        try {
            this.socket.setSoTimeout(1000);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo configurar el timeout UDP", e);
        }
    }

    @Override
    /**
     * Actualiza el valor asociado a `setListener`.
     * @param listener valor del parametro `listener`
     */
    public void setListener(IMessageListener listener) {
        this.listener = listener;
    }

    @Override
    /**
     * Ejecuta la operacion publica `escuchar`.
     */
    public void escuchar() {
        try {
            while (activo && !socket.isClosed()) {
                try {
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    String data = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();

                    if (data.isBlank()) {
                        continue;
                    }

                    try {
                        System.out.println("[DEBUG][UdpReceiver] Datagram recibido desde "
                                + packet.getAddress().getHostAddress() + ":" + packet.getPort()
                                + " bytes=" + packet.getLength());
                        GameMessage message = GameMessage.deserialize(data);

                        InetAddress ipAddress = packet.getAddress();
                        String ip = ipAddress.getHostAddress();
                        int puerto = packet.getPort();

                        if (listener != null) {
                            listener.onMessage(message, ip, puerto);
                        }

                    } catch (Exception e) {
                        System.err.println("Mensaje invÃ¡lido: " + data);
                    }
                } catch (SocketTimeoutException e) {
                    // ciclo de escucha sin ruido de logs
                }
            }
        } catch (Exception e) {
            if (activo && !socket.isClosed()) {
                throw new RuntimeException("Error en recepciÃ³n UDP", e);
            }
        }
    }

    @Override
    /**
     * Ejecuta la operacion publica `detener`.
     */
    public void detener() {
        activo = false;

        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
