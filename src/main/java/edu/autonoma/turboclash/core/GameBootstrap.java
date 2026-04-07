package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;

import java.util.*;

public class GameBootstrap {

    private final GameFactory gameFactory;
    private final WorldFactory worldFactory;
    private final NetworkFactory networkFactory;
    private final GameConfig config;

    public GameBootstrap(GameFactory gameFactory,
                         WorldFactory worldFactory,
                         NetworkFactory networkFactory,
                         GameConfig config) {

        this.gameFactory = gameFactory;
        this.worldFactory = worldFactory;
        this.networkFactory = networkFactory;
        this.config = config;
    }

    public GameContext init(int puertoLocal, String playerName) {

        String playerId = String.valueOf(puertoLocal);

        Player localPlayer = gameFactory.createPlayer(playerId, playerName, puertoLocal);
        List<Player> remotePlayers = new ArrayList<>();

        Match match = gameFactory.createMatch(localPlayer);

        List<Item> items = Collections.synchronizedList(
                new ArrayList<>(worldFactory.createItems())
        );

        List<Obstacle> obstacles = Collections.synchronizedList(
                new ArrayList<>(worldFactory.createObstacles())
        );

        GameRulesManager rules = new GameRulesManager(config.getTargetScore());
        CollisionManager collision = new CollisionManager(rules, config.getCollisionCooldown());

        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        GameSpawner spawner = new GameSpawner(items, obstacles);
        spawner.start();


        GameMessageHandler messageHandler = new GameMessageHandler(remotePlayers);

        UdpPeer peer = networkFactory.createPeer(puertoLocal, remotePlayers, messageHandler);

        GameMessageFactory messageFactory = new GameMessageFactory();
        GameNetworkService network = new GameNetworkService(peer, messageFactory);

        return new GameContext(match, engine, network, obstacles, items, peer);
    }
}