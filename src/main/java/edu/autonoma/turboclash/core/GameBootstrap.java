package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;
import java.util.*;

public class GameBootstrap {

    public GameContext init(int puertoLocal) {
        String id = String.valueOf(puertoLocal);

        Car car = new Car("car-" + id, 50.0, 300.0, "Blue");

        Player localPlayer = new Player("p-" + id, "Player-" + id, car);
        localPlayer.setLives(GameConstants.INITIAL_LIVES);

        List<Player> remotePlayers = new ArrayList<>();
        List<Item> items = createItems();
        List<Obstacle> obstacles = createObstacles();

        Match match = new Match(localPlayer, remotePlayers, GameConstants.DEFAULT_TARGET_SCORE);
        GameRulesManager rules = new GameRulesManager(GameConstants.DEFAULT_TARGET_SCORE);
        CollisionManager collision = new CollisionManager(rules, GameConstants.COLLISION_COOLDOWN);

        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        UdpPeer peer = createPeer(puertoLocal, remotePlayers);
        GameNetworkService network = new GameNetworkService(peer);


        return new GameContext(match, engine, network, obstacles, items, peer);
    }

    private List<Item> createItems() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("coin-1", 400.0, 300.0, 25, 25));
        list.add(new Item("coin-2", 600.0, 150.0, 25, 25));
        list.add(new Item("coin-3", 200.0, 450.0, 25, 25));
        return list;
    }

    private List<Obstacle> createObstacles() {
        List<Obstacle> list = new ArrayList<>();
        list.add(new Obstacle("obs-1", 350.0, 250.0, 50, 50, "OIL"));
        list.add(new Obstacle("obs-2", 550.0, 400.0, 40, 40, "CONE"));
        list.add(new Obstacle("obs-3", 150.0, 100.0, 60, 30, "BARRIER"));
        return list;
    }

    private UdpPeer createPeer(int puertoLocal, List<Player> remotePlayers) {
        UdpPeer peer = new UdpPeer(puertoLocal);
        Map<Integer, String> ips = Map.of(
                5001, "26.8.193.114", 5002, "26.176.207.113",
                5003, "26.14.204.56", 5004, "26.98.94.146"
        );
        ips.forEach((p, ip) -> { if (p != puertoLocal) peer.agregarPeer(ip, p); });
        GameMessageHandler handler = new GameMessageHandler(remotePlayers);
        peer.getReceiver().setListener((msg, ip, port) -> handler.handle(msg));
        peer.iniciar();
        return peer;
    }
}