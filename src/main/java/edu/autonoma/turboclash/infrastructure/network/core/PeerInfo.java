package edu.autonoma.turboclash.infrastructure.network.core;

/**
 * Representa la clase `PeerInfo` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class PeerInfo {

    private final String ip;
    private final int puerto;

    /**
     * Crea una nueva instancia de `PeerInfo`.
     * @param ip valor del parametro `ip`
     * @param puerto valor del parametro `puerto`
     */
    public PeerInfo(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    /**
     * Obtiene el valor asociado a `getIp`.
     * @return resultado de la operacion documentada
     */
    public String getIp() {
        return ip;
    }

    /**
     * Obtiene el valor asociado a `getPuerto`.
     * @return resultado de la operacion documentada
     */
    public int getPuerto() {
        return puerto;
    }
}
