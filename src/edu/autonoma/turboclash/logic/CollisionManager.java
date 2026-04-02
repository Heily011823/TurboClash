package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import java.util.List;


public class CollisionManager {

    private final GameRulesManager rules;


    private long lastCollisionTime = 0;
    private static final long COLLISION_COOLDOWN = 500; // ms

    public CollisionManager(GameRulesManager rules) {
        this.rules = rules;
    }

    public void process(Player localPlayer, List<Player> remotePlayers,
                        List<Item> items, List<Obstacle> obstacles) {

        if (!isValid(localPlayer)) return;

        handleItems(localPlayer, items);
        handleObstacles(localPlayer, obstacles);
        handlePlayerCollisions(localPlayer, remotePlayers);
    }


    // ITEMS (MONEDAS)

    private void handleItems(Player player, List<Item> items) {
        if (items == null) return;

        items.removeIf(item -> {
            if (item.isVisible() && player.getCar().collidesWith(item)) {
                rules.applyCoinReward(player);
                return true;
            }
            return false;
        });
    }

    // OBSTÁCULOS

    private void handleObstacles(Player player, List<Obstacle> obstacles) {
        if (obstacles == null) return;

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isVisible() && player.getCar().collidesWith(obstacle)) {

                if (canApplyCollision()) {
                    rules.applyObstaclePenalty(player);
                }
            }
        }
    }

    // COLISIÓN ENTRE JUGADORES

    private void handlePlayerCollisions(Player local, List<Player> remotes) {
        if (remotes == null) return;

        for (Player remote : remotes) {
            if (remote == null || remote.getCar() == null) continue;

            if (local.getCar().collidesWith(remote.getCar())) {

                if (canApplyCollision()) {
                    rules.handlePlayersCollision(local, remote);
                }
            }
        }
    }


    // CONTROL DE SPAM DE COLISIONES

    private boolean canApplyCollision() {
        long now = System.currentTimeMillis();

        if (now - lastCollisionTime > COLLISION_COOLDOWN) {
            lastCollisionTime = now;
            return true;
        }
        return false;
    }

    private boolean isValid(Player player) {
        return player != null && player.getCar() != null;
    }
}