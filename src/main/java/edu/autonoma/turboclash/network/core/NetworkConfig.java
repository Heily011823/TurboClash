package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.config.GameConfig;

import java.util.HashMap;
import java.util.Map;

public class NetworkConfig {

    public static Map<Integer, String> getPeers() {

        Map<Integer, String> peers = new HashMap<>();

        for (int port = GameConfig.MIN_PORT; port <= GameConfig.MAX_PORT; port++) {
            peers.put(port, GameConfig.DEFAULT_HOST);
        }

        return peers;
    }
}