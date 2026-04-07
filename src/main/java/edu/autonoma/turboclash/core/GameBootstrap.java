package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class GameBootstrap {

    private final GameFactory gameFactory = new GameFactory();
    private final WorldFactory worldFactory = new WorldFactory();
    private final NetworkFactory networkFactory = new NetworkFactory();

    public GameContext init(int puertoLocal, String playerName) {

        String playerId = String.valueOf(puertoLocal);

        Player localPlayer = gameFactory.createPlayer(playerId, playerName, puertoLocal);
        List<Player> remotePlayers = new ArrayList<>();

        Match match = gameFactory.createMatch(localPlayer);

        List<Item> items = worldFactory.createItems();
        List<Obstacle> obstacles = worldFactory.createObstacles();

        GameRulesManager rules = new GameRulesManager(GameConstants.DEFAULT_TARGET_SCORE);
        CollisionManager collision = new CollisionManager(rules, GameConstants.COLLISION_COOLDOWN);

        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        UdpPeer peer = networkFactory.createPeer(puertoLocal, remotePlayers);
        GameMessageFactory messageFactory = new GameMessageFactory();
        GameNetworkService network = new GameNetworkService(peer, messageFactory);

        return new GameContext(match, engine, network, obstacles, items, peer);
    }
}