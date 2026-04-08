package edu.autonoma.turboclash.infrastructure.network.core;

import java.net.DatagramSocket;
import java.util.List;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.domain.rules.PortValidator;
import edu.autonoma.turboclash.exception.InvalidPortException;

/**
 * Representa la responsabilidad de {@code UdpPeer} en la infraestructura de red.
 */
public class UdpPeer {

    private final DatagramSocket socket;
    private final List<PeerInfo> peers = new java.util.concurrent.CopyOnWriteArrayList<>();

    private final IMessageSender sender;
    private final IMessageReceiver receiver;
    private volatile boolean activo;

    private Thread receiverThread;

    /**
     * Crea una nueva instancia de {@code UdpPeer}.
     *
     * @param socket valor del parametro {@code socket}
     * @param sender valor del parametro {@code sender}
     * @param receiver valor del parametro {@code receiver}
     */
    public UdpPeer(DatagramSocket socket, IMessageSender sender, IMessageReceiver receiver) {

        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }

        this.socket = socket;
        this.sender = sender;
        this.receiver = receiver;
        this.activo = true;

        System.out.println("UDP Peer iniciado en puerto: " + socket.getLocalPort());
    }

    /**
     * Agrega {@code Peer}.
     *
     * @param ip direccion IP asociada a la operacion
     * @param puerto valor del parametro {@code puerto}
     */
    public void agregarPeer(String ip, int puerto) {
        try {
            PortValidator.validate(puerto);

            // Evita agregarse a sí mismo
            if (puerto == socket.getLocalPort()) {
                return;
            }

            boolean exists = peers.stream()
                    .anyMatch(peer -> peer.getIp().equals(ip) && peer.getPuerto() == puerto);

            if (!exists) {
                peers.add(new PeerInfo(ip, puerto));
                System.out.println("Peer agregado: " + ip + ":" + puerto);
            }
        } catch (InvalidPortException e) {
            System.err.println("Puerto inválido: " + e.getMessage());
        }
    }

    /**
     * Inicia la operacion principal del metodo.
     */
    public void iniciar() {
        if (!activo || socket.isClosed()) {
            return;
        }

        receiverThread = new Thread(receiver::escuchar);
        receiverThread.start();
    }

    /**
     * Envia {@code ATodos}.
     *
     * @param mensaje valor del parametro {@code mensaje}
     */
    public void enviarATodos(GameMessage mensaje) {
        if (!isActivo()) {
            return;
        }

        for (PeerInfo peer : peers) {
            sender.enviarMensaje(mensaje, peer.getIp(), peer.getPuerto());
        }
    }

    public void enviarATodosExcepto(GameMessage mensaje, String ip, int puerto) {
        if (!isActivo()) {
            return;
        }

        for (PeerInfo peer : peers) {
            if (peer.getIp().equals(ip) && peer.getPuerto() == puerto) {
                continue;
            }

            sender.enviarMensaje(mensaje, peer.getIp(), peer.getPuerto());
        }
    }

    /**
     * Cierra la operacion principal del metodo.
     */
    public void cerrar() {
        activo = false;
        receiver.detener();

        if (receiverThread != null) {
            receiverThread.interrupt();
        }

        if (!socket.isClosed()) {
            socket.close();
        }
    }

    /**
     * Obtiene el valor de {@code Receiver}.
     *
     * @return valor de {@code Receiver}
     */
    public IMessageReceiver getReceiver() {
        return receiver;
    }

    /**
     * Obtiene el valor de {@code Sender}.
     *
     * @return valor de {@code Sender}
     */
    public IMessageSender getSender() {
        return sender;
    }

    public boolean isActivo() {
        return activo && !socket.isClosed();
    }

    public int getPeerCount() {
        return peers.size();
    }
}