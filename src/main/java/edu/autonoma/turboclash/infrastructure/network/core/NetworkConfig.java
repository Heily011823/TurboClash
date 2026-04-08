package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.config.GameConfig;

import java.util.HashMap;
import java.util.Map;

public class NetworkConfig {

    private final GameConfig config;

    public NetworkConfig(GameConfig config) {
        this.config = config;
    }


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