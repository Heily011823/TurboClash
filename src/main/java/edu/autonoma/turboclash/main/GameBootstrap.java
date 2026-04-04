package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.*;

public class GameBootstrap {

    public GameContext init(int puertoLocal) {

        String id = String.valueOf(puertoLocal);

        // 1. Crear el carro y el jugador local
        Car car = new Car("car-" + id, 100, 100,
                GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);

        Player localPlayer = new Player("p-" + id, "Heily-" + id, car);
        localPlayer.setLives(GameConstants.INITIAL_LIVES);

        List<Player> remotePlayers = new ArrayList<>();
        Match match = new Match(localPlayer, remotePlayers, GameConstants.DEFAULT_TARGET_SCORE);

        // 2. Configurar lógica de juego
        GameRulesManager rules = new GameRulesManager(GameConstants.DEFAULT_TARGET_SCORE);
        CollisionManager collision = new CollisionManager(rules, GameConstants.COLLISION_COOLDOWN);

        // 3. Crear los elementos del mundo
        List<Item> items = createItems();
        List<Obstacle> obstacles = createObstacles();

        // 4. Inicializar el motor del juego
        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        // 5. Configurar red
        UdpPeer peer = createPeer(puertoLocal, remotePlayers);
        GameNetworkService network = new GameNetworkService(peer);


        return new GameContext(match, engine, network, obstacles, items, peer);
    }

    private List<Item> createItems() {
        List<Item> list = new ArrayList<>();
        // Estas son las coordenadas donde aparecerán tus monedas
        list.add(new Item("coin-1", 200, 200, 30, 30, 10));
        list.add(new Item("coin-2", 400, 300, 30, 30, 10));
        list.add(new Item("coin-3", 600, 250, 30, 30, 10));
        return list;
    }

    private List<Obstacle> createObstacles() {
        List<Obstacle> list = new ArrayList<>();

        list.add(new Obstacle("obs-1", 300, 200, 50, 50));
        list.add(new Obstacle("obs-2", 500, 350, 50, 50));
        return list;
    }

    private UdpPeer createPeer(int puertoLocal, List<Player> remotePlayers) {
        UdpPeer peer = new UdpPeer(puertoLocal);

        Map<Integer, String> ips = Map.of(
                5001, "26.8.193.114",
                5002, "26.176.207.113",
                5003, "26.14.204.56",
                5004, "26.98.94.146"
        );

        ips.forEach((p, ip) -> {
            if (p != puertoLocal) {
                peer.agregarPeer(ip, p);
            }
        });

        GameMessageHandler handler = new GameMessageHandler(remotePlayers);
        peer.getReceiver().setListener((msg, ip, port) -> handler.handle(msg));

        peer.iniciar();
        return peer;
    }
}