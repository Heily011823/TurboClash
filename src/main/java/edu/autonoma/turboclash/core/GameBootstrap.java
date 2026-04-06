package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.core.*;
import edu.autonoma.turboclash.network.handler.GameMessageHandler;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class GameBootstrap {

    public GameContext init(int puertoLocal, String playerName) {


        String playerId = String.valueOf(puertoLocal);
        CarSkin skin = CarSkinFactory.fromPort(puertoLocal);

        Car car = new Car(playerId, 50.0, 300.0, skin);

        Player localPlayer = new Player(playerId, playerName, car);
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

        list.add(new Item(UUID.randomUUID().toString(), 400.0, 300.0, 25, 25));
        list.add(new Item(UUID.randomUUID().toString(), 600.0, 150.0, 25, 25));
        list.add(new Item(UUID.randomUUID().toString(), 200.0, 450.0, 25, 25));

        return list;
    }


    private List<Obstacle> createObstacles() {
        List<Obstacle> list = new ArrayList<>();

        list.add(new Obstacle(UUID.randomUUID().toString(), 350.0, 250.0, 50, 50, ObstacleType.OIL));
        list.add(new Obstacle(UUID.randomUUID().toString(), 550.0, 400.0, 40, 40, ObstacleType.CONE));
        list.add(new Obstacle(UUID.randomUUID().toString(), 150.0, 100.0, 60, 30, ObstacleType.BARRIER));

        return list;
    }


    private UdpPeer createPeer(int puertoLocal, List<Player> remotePlayers) {
        try {

            DatagramSocket socket = new DatagramSocket(puertoLocal);


            IMessageSender sender = new UdpSender(socket);
            IMessageReceiver receiver = new UdpReceiver(socket);


            UdpPeer peer = new UdpPeer(socket, sender, receiver);


            Map<Integer, String> peersConfig = NetworkConfig.getPeers();
            peersConfig.forEach((port, ip) -> {
                if (port != puertoLocal) {
                    peer.agregarPeer(ip, port);
                }
            });


            GameMessageHandler handler = new GameMessageHandler(remotePlayers);
            peer.getReceiver().setListener((msg, ip, port) -> handler.handle(msg));

            peer.iniciar();
            return peer;

        } catch (SocketException e) {
            throw new RuntimeException("No se pudo iniciar el socket en el puerto " + puertoLocal, e);
        }
    }

}