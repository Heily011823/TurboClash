package edu.autonoma.turboclash.network;

import java.net.DatagramSocket;

public class UdpPeer {

    private DatagramSocket socket;
    private String ipRemota;
    private int puertoLocal;
    private int puertoRemoto;

    private UdpSender sender;
    private UdpReceiver receiver;

    public UdpPeer(String ipRemota, int puertoLocal, int puertoRemoto) {
        this.ipRemota = ipRemota;
        this.puertoLocal = puertoLocal;
        this.puertoRemoto = puertoRemoto;

        try {
            socket = new DatagramSocket(puertoLocal);

            sender = new UdpSender(socket, ipRemota, puertoRemoto);
            receiver = new UdpReceiver(socket);

            System.out.println("UDP Peer iniciado en puerto: " + puertoLocal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciar() {
        receiver.escuchar();
    }

    public void enviar(GameMessage mensaje) {
        sender.enviarMensaje(mensaje);
    }

    public void cerrar() {
        receiver.detener();
        socket.close();
    }
}