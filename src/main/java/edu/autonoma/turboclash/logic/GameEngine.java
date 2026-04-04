package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.GameMessage;

import java.util.List;

public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;
    private final List<Item> items;
    private final List<Obstacle> obstacles;

    public GameEngine(Match match,
                      CollisionManager collisionManager,
                      List<Item> items,
                      List<Obstacle> obstacles) {

        this.match = match;
        this.collisionManager = collisionManager;
        this.items = items;
        this.obstacles = obstacles;
    }


    public void update() {
        if (match.isFinished()) return;

        updateCars();

        processCollisions();

        match.check();
    }


    private void updateCars() {
        for (Player p : match.getPlayers()) {
            if (p != null && p.getCar() != null) {
                p.getCar().update();
            }
        }
    }


    private void processCollisions() {
        collisionManager.process(
                match.getLocalPlayer(),
                match.getRemotePlayers(),
                items,
                obstacles
        );
    }


    public void movePlayer(Player player, double dx, double dy) {
        if (player == null || player.getCar() == null) return;

        player.getCar().move(dx, dy);
    }


    public void syncPlayer(GameMessage msg) {
        if (msg == null) return;

        for (Player p : match.getPlayers()) {
            if (p.getId().equals(msg.playerId)) {
                p.syncFromNetwork(msg.posX, msg.posY, msg.score);
            }
        }
    }
}