package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.util.List;

public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;
    private final List<Item> items;
    private final List<Obstacle> obstacles;
    private static final double DEAD_ZONE_X = 0;
    private static final double WORLD_SPEED = 6.0;

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
        if (match.isFinished()) {
            return;
        }

        updateCars();
        updateWorldObjects();
        processCollisions();
        checkPlayerOut();

        match.check();
    }

    private void updateWorldObjects() {
        for (Item item : items) {
            item.setPosition(item.getX() - WORLD_SPEED, item.getY());
        }

        for (Obstacle obs : obstacles) {
            obs.setPosition(obs.getX() - WORLD_SPEED, obs.getY());
        }

        items.removeIf(item -> item.getX() < -100);
        obstacles.removeIf(obs -> obs.getX() < -100);
    }

    private void updateCars() {
        for (Player p : match.getPlayers()) {
            if (p.getCar() != null) {
                p.getCar().updateDebuff();
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
            if (p.getId().equals(msg.getPlayerId())) {
                p.syncFromNetwork(msg.getPosX(), msg.getPosY(), msg.getScore());
            }
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    private void checkPlayerOut() {
        Player local = match.getLocalPlayer();

        if (local == null || local.getCar() == null) return;

        if (local.getCar().getX() <= DEAD_ZONE_X) {

            match.setFinished(null);
        }
    }
}
