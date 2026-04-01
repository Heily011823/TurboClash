package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.logic.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.ArrayList;
import java.util.List;

public class GameApplication {

    public void start() {

        // Crear carros
        Car car1 = new Car("c1", 100, 100, 40, 40);
        Car car2 = new Car("c2", 200, 200, 40, 40);

        // Crear jugadores
        Player p1 = new Player("p1", "Heily", car1);
        Player p2 = new Player("p2", "Remote", car2);

        // Crear match
        Match match = new Match(p1, p2, 100);

        // Items y obstáculos
        List<Item> items = new ArrayList<>();
        items.add(new Item("i1", 150, 150, 20, 20, 10));

        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle("o1", 300, 300, 30, 30, 5));

        //  Managers
        CollisionManager collision = new CollisionManager();

        //  Engine
        GameEngine engine = new GameEngine(match, collision, items, obstacles);

        //  Red (UDP)
        UdpPeer peer = new UdpPeer("26.8.193.114", 5000, 5001);
        peer.iniciar();

        // LOOP DEL JUEGO (
        while (!match.isFinished()) {
            engine.update();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Ganador: " +
                (match.getWinner() != null ? match.getWinner().getName() : "Empate"));
    }
}