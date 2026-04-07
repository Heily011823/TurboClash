package edu.autonoma.turboclash.network.core;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import edu.autonoma.turboclash.network.message.GameMessage;
import edu.autonoma.turboclash.validation.PortValidator;
import edu.autonoma.turboclash.exception.InvalidPortException;

public class UdpPeer {

    private final DatagramSocket socket;
    private final List<PeerInfo> peers;

    private final IMessageSender sender;
    private final IMessageReceiver receiver;

    private Thread receiverThread;


    public UdpPeer(DatagramSocket socket, IMessageSender sender, IMessageReceiver receiver) {

        if (socket == null) {
            throw new IllegalArgumentException("El socket no puede ser nulo");
        }

        this.socket = socket;
        this.peers = new ArrayList<>();
        this.sender = sender;
        this.receiver = receiver;

        System.out.println("UDP Peer iniciado en puerto: " + socket.getLocalPort());
    }

    public void agregarPeer(String ip, int puerto) {
        try {
            PortValidator.validate(puerto);
            peers.add(new PeerInfo(ip, puerto));
        } catch (InvalidPortException e) {
            System.err.println("Puerto inválido: " + e.getMessage());
        }
    }

    public void iniciar() {
        receiverThread = new Thread(receiver::escuchar);
        receiverThread.start();
    }

    public void enviarATodos(GameMessage mensaje) {
        for (PeerInfo peer : peers) {
            sender.enviarMensaje(mensaje, peer.getIp(), peer.getPuerto());
        }
    }

    public void cerrar() {
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
}