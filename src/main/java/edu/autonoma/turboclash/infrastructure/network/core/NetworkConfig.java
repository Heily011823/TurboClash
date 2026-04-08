package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.config.GameConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa la responsabilidad de {@code NetworkConfig} en la infraestructura de red.
 */
public class NetworkConfig {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de {@code NetworkConfig}.
     *
     * @param config valor del parametro {@code config}
     */
    public NetworkConfig(GameConfig config) {
        this.config = config;
    }


    /**
     * Obtiene el valor de {@code Ports}.
     *
     * @return valor de {@code Ports}
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
