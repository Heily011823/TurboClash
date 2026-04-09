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

    public void agregarPeer(String ip, int puerto) {
        try {
            PortValidator.validate(puerto);

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

    public synchronized void iniciar() {
        if (!activo || socket.isClosed()) {
            return;
        }

        if (receiverThread != null && receiverThread.isAlive()) {
            System.out.println("Receiver ya estaba iniciado en puerto: " + socket.getLocalPort());
            return;
        }

        receiverThread = new Thread(receiver::escuchar);
        receiverThread.setName("UdpReceiver-" + socket.getLocalPort());
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

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

    public IMessageReceiver getReceiver() {
        return receiver;
    }

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