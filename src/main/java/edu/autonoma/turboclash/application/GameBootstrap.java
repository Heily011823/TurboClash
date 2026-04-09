package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.*;
import edu.autonoma.turboclash.domain.services.*;
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

        CollisionListener listener = new SoundCollisionListener();

        CollisionManager collisionManager = new CollisionManager(listener);

        GameEngine engine = new GameEngine(
                match,
                collisionManager,
                items,
                obstacles
        );

        GameSpawner spawner = new GameSpawner(items, obstacles);
        spawner.start();

        GameMessageHandler messageHandler = new GameMessageHandler(match);

        GameMessageFactory messageFactory = new GameMessageFactory();

        UdpPeer peer = networkFactory.createPeer(
                puertoLocal,
                localPlayer,
                match,
                messageHandler,
                messageFactory
        );

        messageHandler.setPeer(peer);
        messageHandler.setMessageFactory(messageFactory);

        peer.iniciar();

        NetworkConfig networkConfig = new NetworkConfig(config);

        GameNetworkService network =
                new GameNetworkService(peer, messageFactory, networkConfig);

        GameContext context = new GameContext(
                match,
                engine,
                network,
                obstacles,
                items,
                peer,
                new GameRulesManager(100),
                new GameResultManager()
        );

        context.setLocalPlayer(localPlayer);

        return context;
    }
}