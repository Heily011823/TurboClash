package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.*;

public class GameApplication {

    public void start() {
        // --- CONFIGURACIÓN DEL JUGADOR LOCAL ---
        Car carLocal = new Car("c1", 100, 100, 40, 40);
        Player localPlayer = new Player("p1", "Heily", carLocal);

        List<Player> players = new ArrayList<>();
        players.add(localPlayer);

        Match match = new Match(localPlayer, players, 100);

        List<Item> items = new ArrayList<>();
        items.add(new Item("i1", 150, 150, 20, 20, 10));

        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle("o1", 300, 300, 30, 30, 5));

        CollisionManager collision = new CollisionManager();

        // --- CONFIGURACIÓN DE RED (RADMIN VPN) ---
        int puertoLocal = Integer.parseInt(System.getProperty("puerto", "5000"));
        UdpPeer peer = new UdpPeer(puertoLocal);

        // Agregamos a todos los compañeros según la captura de Radmin
        // 1. DESKTOP-FMB7FRC
        if (puertoLocal != 5001)
            peer.agregarPeer("26.8.193.114", 5001);

        // 2. DESKTOP-U4JVA7D
        if (puertoLocal != 5002)
            peer.agregarPeer("26.176.207.113", 5002);

        // 3. LAPTOP-3MG8IKDR
        if (puertoLocal != 5003)
            peer.agregarPeer("26.14.204.56", 5003);

        // 4. TU PC (DESKTOP-TD25AUP)
        if (puertoLocal != 5004)
            peer.agregarPeer("26.98.94.146", 5004);

        List<UdpPeer> peers = new ArrayList<>();
        peers.add(peer);

        GameEngine engine = new GameEngine(match, collision, items, obstacles, peers);

        // --- LÓGICA DE RECEPCIÓN ---
        peer.getReceiver().setListener((msg, ip, port) -> {
            switch (msg.type) {
                case PLAYER_JOINED:
                    boolean existe = false;
                    for (Player p : players) {
                        if (p.getId().equals(msg.playerId)) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        Car car = new Car("c_" + msg.playerId, msg.posX, msg.posY, 40, 40);
                        Player nuevo = new Player(msg.playerId, msg.playerName, car);
                        players.add(nuevo);
                    }
                    break;

                case MOVEMENT:
                    for (Player p : players) {
                        if (p.getId().equals(msg.playerId)) {
                            p.getCar().moveTo(msg.posX, msg.posY);
                        }
                    }
                    break;

                case SCORE_UPDATE:
                    for (Player p : players) {
                        if (p.getId().equals(msg.playerId)) {
                            p.setScore(msg.score);
                        }
                    }
                    break;

                case PLAYER_LEFT:
                    players.removeIf(p -> p.getId().equals(msg.playerId));
                    break;

                default:
                    break;
            }
        });

        peer.iniciar();

        // --- ENVIAR MENSAJE DE ENTRADA (JOIN) ---
        GameMessage joinMsg = new GameMessage();
        joinMsg.type = MessageType.PLAYER_JOINED;
        joinMsg.playerId = localPlayer.getId();
        joinMsg.playerName = localPlayer.getName();
        joinMsg.posX = localPlayer.getCar().getX();
        joinMsg.posY = localPlayer.getCar().getY();
        joinMsg.score = 0;
        joinMsg.time = System.currentTimeMillis();
        joinMsg.event = "";

        peer.enviarATodos(joinMsg);

        // --- LOOP PRINCIPAL DEL JUEGO ---
        while (!match.isFinished()) {
            engine.update();

            GameMessage msg = new GameMessage();
            msg.type = MessageType.MOVEMENT;
            msg.playerId = localPlayer.getId();
            msg.playerName = localPlayer.getName();
            msg.posX = localPlayer.getCar().getX();
            msg.posY = localPlayer.getCar().getY();
            msg.score = localPlayer.getCurrentPoints();
            msg.time = System.currentTimeMillis();
            msg.event = "";

            try {
                peer.enviarATodos(msg);
            } catch (Exception e) {
                // Si falla el envío a un peer, imprimimos el error pero no matamos el juego
                System.err.println("Error enviando actualización: " + e.getMessage());
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // --- SALIDA Y CIERRE ---
        GameMessage leaveMsg = new GameMessage();
        leaveMsg.type = MessageType.PLAYER_LEFT;
        leaveMsg.playerId = localPlayer.getId();
        leaveMsg.playerName = localPlayer.getName();

        peer.enviarATodos(leaveMsg);
        peer.cerrar();

        System.out.println("Ganador: " +
                (match.getWinner() != null ? match.getWinner().getName() : "Empate"));
    }
}