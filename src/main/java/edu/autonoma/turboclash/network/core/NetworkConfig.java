package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.config.GameConfig;

import java.util.HashMap;
import java.util.Map;

public class NetworkConfig {

    private final GameConfig config;

    public NetworkConfig(GameConfig config) {
        this.config = config;
    }

    public Map<Integer, String> getPeers() {

        Map<Integer, String> peers = new HashMap<>();

        for (int port = config.getMinPort(); port <= config.getMaxPort(); port++) {
            peers.put(port, config.getDefaultHost());
        }

        return peers;
    }
}