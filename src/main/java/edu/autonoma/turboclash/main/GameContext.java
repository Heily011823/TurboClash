package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.List;

public class GameContext {

    public final Match match;
    public final GameEngine engine;
    public final GameNetworkService network;
    public final List<Obstacle> obstacles;
    public final List<Item> items;
    public final UdpPeer peer;

    public GameContext(Match match, GameEngine engine,
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
}