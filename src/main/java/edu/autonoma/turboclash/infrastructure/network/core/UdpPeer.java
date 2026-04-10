package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.domain.rules.PortValidator;
import edu.autonoma.turboclash.exception.InvalidPortException;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.net.DatagramSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Representa la clase `UdpPeer` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class UdpPeer {

    private final DatagramSocket socket;
    private final List<PeerInfo> peers = new CopyOnWriteArrayList<>();

    private final IMessageSender sender;
    private final IMessageReceiver receiver;
    private volatile boolean activo;
    private Thread receiverThread;

    /**
     * Crea una nueva instancia de `UdpPeer`.
     * @param socket valor del parametro `socket`
     * @param sender valor del parametro `sender`
     * @param receiver valor del parametro `receiver`
     */
    public UdpPeer(DatagramSocket socket, IMessageSender sender, IMessageReceiver receiver) {

        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }

        this.socket = socket;
        this.sender = sender;
        this.receiver = receiver;
        this.activo = true;

    }

    /**
     * Ejecuta la operacion publica `iniciar`.
     */
    public synchronized void iniciar() {
        if (!activo || socket.isClosed()) {
            return;
        }

        if (receiverThread != null && receiverThread.isAlive()) {
            return;
        }

        receiverThread = new Thread(receiver::escuchar);
        receiverThread.setName("UdpReceiver-" + socket.getLocalPort());
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    /**
     * Ejecuta la operacion publica `agregarPeer`.
     * @param ip valor del parametro `ip`
     * @param puerto valor del parametro `puerto`
     */
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
            }
        } catch (InvalidPortException e) {
            System.err.println("Puerto invÃ¡lido: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la operacion publica `enviarATodos`.
     * @param mensaje valor del parametro `mensaje`
     */
    public void enviarATodos(GameMessage mensaje) {
        if (!isActivo()) {
            return;
        }

        for (PeerInfo peer : peers) {
            sender.enviarMensaje(mensaje, peer.getIp(), peer.getPuerto());
        }
    }

    /**
     * Ejecuta la operacion publica `enviarATodosExcepto`.
     * @param mensaje valor del parametro `mensaje`
     * @param ip valor del parametro `ip`
     * @param puerto valor del parametro `puerto`
     */
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
     * Ejecuta la operacion publica `cerrar`.
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
     * Obtiene el valor asociado a `getSender`.
     * @return resultado de la operacion documentada
     */
    public IMessageSender getSender() {
        return sender;
    }

    /**
     * Obtiene el valor asociado a `getReceiver`.
     * @return resultado de la operacion documentada
     */
    public IMessageReceiver getReceiver() {
        return receiver;
    }

    /**
     * Indica la condicion evaluada por `isActivo`.
     * @return resultado de la operacion documentada
     */
    public boolean isActivo() {
        return activo && !socket.isClosed();
    }

    /**
     * Obtiene el valor asociado a `getPeerCount`.
     * @return resultado de la operacion documentada
     */
    public int getPeerCount() {
        return peers.size();
    }

    /**
     * Obtiene el valor asociado a `getLocalPort`.
     * @return resultado de la operacion documentada
     */
    public int getLocalPort() {
        return socket.getLocalPort();
    }
}
