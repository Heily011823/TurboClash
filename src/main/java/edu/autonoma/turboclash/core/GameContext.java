package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.GameEngine;
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.network.core.GameNetworkService;
import edu.autonoma.turboclash.network.core.UdpPeer;

import java.util.List;
import java.util.Collections;

public class GameContext {

    private final Match match;
    private final GameEngine engine;
    private final GameNetworkService network;
    private final List<Obstacle> obstacles;
    private final List<Item> items;
    private final UdpPeer peer;

    public GameContext(Match match,
                       GameEngine engine,
                       GameNetworkService network,
                       List<Obstacle> obstacles,
                       List<Item> items,
                       UdpPeer peer) {

        this.match = match;
        this.engine = engine;
        this.network = network;
        this.obstacles = obstacles;
        this.items = items;
        this.peer = peer;
    }

    public Match getMatch() {
        return match;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public GameNetworkService getNetwork() {
        return network;
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public UdpPeer getPeer() {
        return peer;
    }
}