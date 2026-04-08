package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.*;
import edu.autonoma.turboclash.domain.services.*;
import edu.autonoma.turboclash.infrastructure.*;
import edu.autonoma.turboclash.infrastructure.network.core.*;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.sound.IAudioService;
import edu.autonoma.turboclash.infrastructure.sound.SoundCollisionListener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

        Match match = new Match(
                localPlayer,
                remotePlayers,
                config.getTargetScore()
        );

        List<Item> items = new CopyOnWriteArrayList<>(worldFactory.createItems());
        List<Obstacle> obstacles = new CopyOnWriteArrayList<>(worldFactory.createObstacles());


        CollisionListener listener = new SoundCollisionListener();

        CollisionManager collisionManager =
                new CollisionManager(listener);


        GameEngine engine = new GameEngine(
                match,
                collisionManager,
                items,
                obstacles
        );


        GameSpawner spawner = new GameSpawner(items, obstacles);
        spawner.start();


        GameMessageHandler messageHandler =
                new GameMessageHandler(remotePlayers, match);

        GameMessageFactory messageFactory = new GameMessageFactory();

        UdpPeer peer = networkFactory.createPeer(
                puertoLocal,
                localPlayer,
                remotePlayers,
                messageHandler,
                messageFactory
        );

        GameNetworkService network =
                new GameNetworkService(peer, messageFactory);

        network.sendJoin(localPlayer);


        return new GameContext(
                match,
                engine,
                network,
                obstacles,
                items,
                peer
        );
    }
}