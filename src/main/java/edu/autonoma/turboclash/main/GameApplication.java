package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;
import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.view.*;

import java.util.*;

public class GameApplication {

    public void start() {

        // --- INPUT ---
        KeyboardInput keyboardInput = new KeyboardInput();
        MouseInput mouseInput = new MouseInput();

        // --- VENTANA ---
        GameWindowFrame frame = new GameWindowFrame(keyboardInput, mouseInput);
        GameWindow window = frame.getView();

        // --- JUGADOR LOCAL ---
        Car carLocal = new Car("c1", 100, 100, 40, 40);
        Player localPlayer = new Player("p1", "Heily", carLocal);

        List<Player> remotePlayers = new ArrayList<>();
        Match match = new Match(localPlayer, remotePlayers, 100);

        List<Item> items = new ArrayList<>();
        items.add(new Item("i1", 150, 150, 20, 20, 10));

        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle("o1", 300, 300, 30, 30));

        GameRulesManager rules = new GameRulesManager(100);
        CollisionManager collision = new CollisionManager(rules);

        // --- RED ---
        int puertoLocal = Integer.parseInt(System.getProperty("puerto", "5000"));
        UdpPeer peer = new UdpPeer(puertoLocal);

        configurarPeers(peer, puertoLocal);

        List<UdpPeer> peers = new ArrayList<>();
        peers.add(peer);

        GameEngine engine = new GameEngine(match, collision, items, obstacles, peers);

        // --- CONTROL ---
        boolean usaTeclado = (puertoLocal == 5001 || puertoLocal == 5002);

        // --- RECEPCIÓN ---
        GameMessageHandler handler = new GameMessageHandler(remotePlayers);
        peer.getReceiver().setListener((msg, ip, port) -> {
            handler.handle(msg);
        });

        peer.iniciar();

        // --- RED ENVÍO ---
        GameNetworkService networkService = new GameNetworkService(peer);

        // --- JOIN ---
        networkService.sendJoin(localPlayer);

        // --- LOOP ---
        while (!match.isFinished()) {

            // 🎮 INPUT
            if (usaTeclado) {
                keyboardInput.update(localPlayer.getCar());
            } else {
                mouseInput.update(localPlayer.getCar());
            }

            // 🧠 LÓGICA
            engine.update();

            Player winner = rules.getWinner(match, null);
            if (winner != null) {
                match.setFinished(winner);
                break;
            }

            // 🖼️ ACTUALIZAR VISTA
            List<Car> cars = new ArrayList<>();
            cars.add(localPlayer.getCar());

            for (Player p : remotePlayers) {
                cars.add(p.getCar());
            }

            window.actualizarCarros(cars);

            // 🌐 RED
            networkService.sendMovement(localPlayer);

            sleep();
        }

        // --- SALIDA ---
        networkService.sendLeave(localPlayer);
        peer.cerrar();

        System.out.println("Ganador: " +
                (match.getWinner() != null ? match.getWinner().getName() : "Empate"));
    }

    // -------------------- MÉTODOS AUXILIARES --------------------

    private void configurarPeers(UdpPeer peer, int puertoLocal) {

        if (puertoLocal != 5001)
            peer.agregarPeer("26.8.193.114", 5001);

        if (puertoLocal != 5002)
            peer.agregarPeer("26.176.207.113", 5002);

        if (puertoLocal != 5003)
            peer.agregarPeer("26.14.204.56", 5003);

        if (puertoLocal != 5004)
            peer.agregarPeer("26.98.94.146", 5004);
    }

    private void sleep() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}