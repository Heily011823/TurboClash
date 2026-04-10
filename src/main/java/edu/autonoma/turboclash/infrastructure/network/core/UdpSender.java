package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Representa la clase `UdpSender` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class UdpSender implements IMessageSender {

    private final DatagramSocket socket;

    /**
     * Crea una nueva instancia de `UdpSender`.
     * @param socket valor del parametro `socket`
     */
    public UdpSender(DatagramSocket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }
        this.socket = socket;
    }

    @Override
    /**
     * Ejecuta la operacion publica `enviarMensaje`.
     * @param message valor del parametro `message`
     * @param ipDestino valor del parametro `ipDestino`
     * @param puertoDestino valor del parametro `puertoDestino`
     */
    public void enviarMensaje(GameMessage message, String ipDestino, int puertoDestino) {

        if (message == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }

        if (ipDestino == null || ipDestino.isEmpty()) {
            throw new IllegalArgumentException("IP destino invÃƒÂ¡lida");
        }

        if (socket.isClosed()) {
            return;
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

        } catch (SocketException e) {
            if (socket.isClosed()) {
                return;
            }
            throw new RuntimeException("Error enviando mensaje UDP", e);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando mensaje UDP", e);
        }
    }

    private byte[] serialize(GameMessage message) {
        return message.serialize().getBytes();
    }
}
