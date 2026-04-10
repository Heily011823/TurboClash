package edu.autonoma.turboclash.infrastructure.network.config;

/**
 * Representa la clase `PeerConfigEntry` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class PeerConfigEntry {

    private String nombre;
    private String ip;
    private int puerto;

    /**
     * Obtiene el valor asociado a `getNombre`.
     * @return resultado de la operacion documentada
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Actualiza el valor asociado a `setNombre`.
     * @param nombre valor del parametro `nombre`
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el valor asociado a `getIp`.
     * @return resultado de la operacion documentada
     */
    public String getIp() {
        return ip;
    }

    /**
     * Actualiza el valor asociado a `setIp`.
     * @param ip valor del parametro `ip`
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Obtiene el valor asociado a `getPuerto`.
     * @return resultado de la operacion documentada
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Actualiza el valor asociado a `setPuerto`.
     * @param puerto valor del parametro `puerto`
     */
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
}
