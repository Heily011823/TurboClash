package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.List;

public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;

    private final List<Item> items;
    private final List<Obstacle> obstacles;

    private static final double WORLD_SPEED = 6.0;
    private static final double DEAD_ZONE_X = 0;

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

        updatePlayers();
        updateWorld();
        processCollisions();
        checkPlayerOut();

        match.check();
    }

    private void updatePlayers() {
        for (Player p : match.getPlayers()) {
            if (p.getCar() != null) {
                p.getCar().updateDebuff();
            }
        }
    }

    private void updateWorld() {
        for (Item item : items) {
            item.setPosition(item.getX() - WORLD_SPEED, item.getY());
        }

        for (Obstacle obs : obstacles) {
            obs.setPosition(obs.getX() - WORLD_SPEED, obs.getY());
        }

        items.removeIf(item -> item.getX() < -100);
        obstacles.removeIf(obs -> obs.getX() < -100);
    }

    private void processCollisions() {
        collisionManager.process(
                match.getLocalPlayer(),
                match.getRemotePlayers(),
                items,
                obstacles
        );
    }

    private void checkPlayerOut() {
        Player local = match.getLocalPlayer();

        if (local == null || local.getCar() == null) return;

        if (local.getCar().getX() <= DEAD_ZONE_X) {
            match.setFinished(null);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}