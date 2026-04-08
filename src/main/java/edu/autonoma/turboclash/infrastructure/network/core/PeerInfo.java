package edu.autonoma.turboclash.infrastructure.network.core;

public class PeerInfo {

    private final String ip;
    private final int puerto;

    public PeerInfo(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }
}