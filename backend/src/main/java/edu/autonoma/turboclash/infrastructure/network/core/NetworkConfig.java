package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.config.GameConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa la clase `NetworkConfig` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class NetworkConfig {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de `NetworkConfig`.
     * @param config valor del parametro `config`
     */
    public NetworkConfig(GameConfig config) {
        this.config = config;
    }


    /**
     * Obtiene el valor asociado a `getPorts`.
     * @return resultado de la operacion documentada
     */
    public int[] getPorts() {
        int size = config.getMaxPort() - config.getMinPort() + 1;
        int[] ports = new int[size];

        int index = 0;
        for (int port = config.getMinPort(); port <= config.getMaxPort(); port++) {
            ports[index++] = port;
        }

        return ports;
    }
}
