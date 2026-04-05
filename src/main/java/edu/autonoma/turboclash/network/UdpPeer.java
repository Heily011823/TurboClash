package edu.autonoma.turboclash.network;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import edu.autonoma.turboclash.validation.PortValidator;
import edu.autonoma.turboclash.exception.InvalidPortException;

public class UdpPeer {

    private DatagramSocket socket;
    private int puertoLocal;

    private List<PeerInfo> peers = new ArrayList<>();

    private UdpSender sender;
    private UdpReceiver receiver;

    public UdpPeer(int puertoLocal) {
        this.puertoLocal = puertoLocal;

        try {
            socket = new DatagramSocket(puertoLocal);

            sender = new UdpSender(socket);
            receiver = new UdpReceiver(socket);

            System.out.println("UDP Peer iniciado en puerto: " + puertoLocal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarPeer(String ip, int puerto) {
        try {
            PortValidator.validate(puerto);

            peers.add(new PeerInfo(ip, puerto));
            System.out.println("Peer agregado: " + ip + ":" + puerto);

        } catch (InvalidPortException e) {
            System.err.println("Puerto inválido: " + e.getMessage());
        }
    }

    public void iniciar() {
        new Thread(() -> receiver.escuchar()).start();
    }


    public void enviarATodos(GameMessage mensaje) {
        for (PeerInfo peer : peers) {
            sender.enviarMensaje(mensaje, peer.ip, peer.puerto);
        }
    }


    public void enviar(GameMessage mensaje) {
        enviarATodos(mensaje);
    }

    public void cerrar() {
        receiver.detener();
        socket.close();
    }

    public UdpReceiver getReceiver() {
        return receiver;
    }

    private static class PeerInfo {
        String ip;
        int puerto;

        public PeerInfo(String ip, int puerto) {
            this.ip = ip;
            this.puerto = puerto;
        }
    }
}