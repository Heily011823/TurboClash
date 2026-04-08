package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.GameFactory;
import edu.autonoma.turboclash.infrastructure.NetworkFactory;
import edu.autonoma.turboclash.infrastructure.WorldFactory;
import edu.autonoma.turboclash.domain.services.*;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.sound.IAudioService;

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
                         GameConfig config,
                         IAudioService audioService) {

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
        Match match = new Match(localPlayer, remotePlayers, config.getTargetScore());


        List<Item> items = new CopyOnWriteArrayList<>(worldFactory.createItems());
        List<Obstacle> obstacles = new CopyOnWriteArrayList<>(worldFactory.createObstacles());


        GameRulesManager rules = new GameRulesManager(config.getTargetScore());
        CollisionManager collisionManager =
                new CollisionManager(rules, config.getCollisionCooldown(), audioService);


        PlayerService playerService = new PlayerService();
        WorldService worldService = new WorldService();
        CollisionManager collisionService = new CollisionManager(collisionManager);
        RuleService ruleService = new RuleService();


        GameEngine engine = new GameEngine(
                match,
                playerService,
                worldService,
                collisionService,
                ruleService,
                items,
                obstacles
        );


        GameSpawner spawner = new GameSpawner(items, obstacles);
        spawner.start();


        GameMessageHandler messageHandler = new GameMessageHandler(remotePlayers, match);
        GameMessageFactory messageFactory = new GameMessageFactory();

        UdpPeer peer = networkFactory.createPeer(
                puertoLocal,
                localPlayer,
                remotePlayers,
                messageHandler,
                messageFactory
        );

        GameNetworkService network = new GameNetworkService(peer, messageFactory);

        network.sendJoin(localPlayer);

        return new GameContext(match, engine, network, obstacles, items, peer);
    }
}