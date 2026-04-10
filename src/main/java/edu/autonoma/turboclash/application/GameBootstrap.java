package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.CollisionManager;
import edu.autonoma.turboclash.domain.services.CompositeCollisionListener;
import edu.autonoma.turboclash.domain.services.GameCollisionHandler;
import edu.autonoma.turboclash.domain.services.GameEngine;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameSpawner;
import edu.autonoma.turboclash.infrastructure.GameFactory;
import edu.autonoma.turboclash.infrastructure.NetworkFactory;
import edu.autonoma.turboclash.infrastructure.WorldFactory;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.core.NetworkConfig;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.handler.GameMessageHandler;
import edu.autonoma.turboclash.infrastructure.sound.IAudioService;
import edu.autonoma.turboclash.infrastructure.sound.SoundCollisionListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Coordina la inicializacion de {@code GameBootstrap} en la capa de aplicacion.
 */
public class GameBootstrap {

    private final GameFactory gameFactory;
    private final WorldFactory worldFactory;
    private final NetworkFactory networkFactory;
    private final GameConfig config;
    private final IAudioService audioService;

    /**
     * Crea una nueva instancia de {@code GameBootstrap}.
     *
     * @param gameFactory valor del parametro {@code gameFactory}
     * @param worldFactory valor del parametro {@code worldFactory}
     * @param networkFactory valor del parametro {@code networkFactory}
     * @param config valor del parametro {@code config}
     * @param audioService valor del parametro {@code audioService}
     */
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

    /**
     * Inicializa la operacion principal del metodo.
     *
     * @param puertoLocal valor del parametro {@code puertoLocal}
     * @param playerName valor del parametro {@code playerName}
     * @return resultado de la operacion {@code init}
     */
    public GameContext init(int puertoLocal, String playerName) {

        String playerId = String.valueOf(puertoLocal);

        Player localPlayer = gameFactory.createPlayer(playerId, playerName, puertoLocal);

        Match match = new Match(
                localPlayer,
                new CopyOnWriteArrayList<>(),
                config.getTargetScore()
        );

        List<Item> items = new CopyOnWriteArrayList<>(worldFactory.createItems());
        List<Obstacle> obstacles = new CopyOnWriteArrayList<>(worldFactory.createObstacles());

        GameRulesManager rulesManager = new GameRulesManager(config.getTargetScore());

        CollisionListener listener = new CompositeCollisionListener(
                new GameCollisionHandler(rulesManager),
                new SoundCollisionListener()
        );

        CollisionManager collisionManager = new CollisionManager(listener);

        GameEngine engine = new GameEngine(
                match,
                collisionManager,
                items,
                obstacles
        );

        GameSpawner spawner = new GameSpawner(items, obstacles);

        GameMessageFactory messageFactory = new GameMessageFactory();
        GameMessageHandler messageHandler = new GameMessageHandler(match, null);

        UdpPeer peer = networkFactory.createPeer(
                puertoLocal,
                localPlayer,
                match,
                messageHandler,
                messageFactory
        );

        NetworkConfig networkConfig = new NetworkConfig(config);
        GameNetworkService network = new GameNetworkService(peer, messageFactory, networkConfig);
        GameResultManager resultManager = new GameResultManager();
        AuthoritativeMatchCoordinator coordinator = new AuthoritativeMatchCoordinator(
                match,
                engine,
                network,
                rulesManager,
                resultManager,
                items,
                obstacles,
                spawner,
                puertoLocal
        );

        messageHandler.setCoordinator(coordinator);
        messageHandler.setPeer(peer);
        messageHandler.setMessageFactory(messageFactory);

        peer.iniciar();

        GameContext context = new GameContext(
                match,
                engine,
                network,
                obstacles,
                items,
                peer,
                rulesManager,
                resultManager,
                coordinator
        );

        context.setLocalPlayer(localPlayer);

        return context;
    }
}
