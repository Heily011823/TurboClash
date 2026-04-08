package edu.autonoma.turboclash.infrastructure.network.core;

/**
 * Representa la responsabilidad de {@code PeerInfo} en la infraestructura de red.
 */
public class PeerInfo {

    private final String ip;
    private final int puerto;

    /**
     * Crea una nueva instancia de {@code PeerInfo}.
     *
     * @param ip direccion IP asociada a la operacion
     * @param puerto valor del parametro {@code puerto}
     */
    public PeerInfo(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    /**
     * Obtiene el valor de {@code Ip}.
     *
     * @return valor de {@code Ip}
     */
    public String getIp() {
        return ip;
    }

    /**
     * Obtiene el valor de {@code Puerto}.
     *
     * @return valor de {@code Puerto}
     */
    public int getPuerto() {
        return puerto;
    }
}
