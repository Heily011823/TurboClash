package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.sound.IAudioService;
import edu.autonoma.turboclash.sound.SoundManager;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.*;

public class GameBootstrap {

    private final GameFactory gameFactory;
    private final WorldFactory worldFactory;
    private final NetworkFactory networkFactory;
    private final GameConfig config;
    private final IAudioService audioService;

    public GameBootstrap(GameFactory gameFactory,
                         WorldFactory worldFactory,
                         NetworkFactory networkFactory,
                         GameConfig config, IAudioService audioService) {

        this.gameFactory = gameFactory;
        this.worldFactory = worldFactory;
        this.networkFactory = networkFactory;
        this.config = config;
        this.audioService = audioService;
    }

    public GameContext init(int puertoLocal, String playerName) {

        String playerId = String.valueOf(puertoLocal);

        Player localPlayer = gameFactory.createPlayer(playerId, playerName, puertoLocal);
        List<Player> remotePlayers = new ArrayList<>();

        Match match = gameFactory.createMatch(localPlayer);

        List<Item> items = new CopyOnWriteArrayList<>(
                worldFactory.createItems()
        );

        List<Obstacle> obstacles = new CopyOnWriteArrayList<>(
                worldFactory.createObstacles()
        );

        GameRulesManager rules = new GameRulesManager(config.getTargetScore());
        CollisionManager collision = new CollisionManager(rules, config.getCollisionCooldown(), audioService);

        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        GameSpawner spawner = new GameSpawner(items, obstacles);
        spawner.start();


        GameMessageHandler messageHandler = new GameMessageHandler(remotePlayers);

        GameMessageFactory messageFactory = new GameMessageFactory();

        UdpPeer peer = networkFactory.createPeer(
                puertoLocal,
                localPlayer,
                remotePlayers,
                messageHandler,
                messageFactory
        );


        GameNetworkService network = new GameNetworkService(peer, messageFactory);

        return new GameContext(match, engine, network, obstacles, items, peer);
    }
}