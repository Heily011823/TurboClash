package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import java.util.List;

public class CollisionManager {

    private final GameRulesManager rules;
    private final long collisionCooldown;

    private long lastCollisionTime = 0;

    public CollisionManager(GameRulesManager rules, long collisionCooldown) {
        this.rules = rules;
        this.collisionCooldown = collisionCooldown;
    }

    public void process(Player localPlayer, List<Player> remotePlayers,
                        List<Item> items, List<Obstacle> obstacles) {

        if (!isValid(localPlayer)) return;

        handleItems(localPlayer, items);
        handleObstacles(localPlayer, obstacles);
        handlePlayerCollisions(localPlayer, remotePlayers);
    }

    // ======================
    // ITEMS (MONEDAS)
    // ======================
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

    // ======================
    // OBSTÁCULOS
    // ======================
    private void handleObstacles(Player player, List<Obstacle> obstacles) {
        if (obstacles == null) return;

        Car car = player.getCar();

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isVisible() && car.collidesWith(obstacle)) {


                resolveCollision(car);

                if (canApplyCollision()) {
                    rules.applyObstaclePenalty(player);
                }
            }
        }
    }

    // ======================
    // COLISIÓN ENTRE JUGADORES
    // ======================
    private void handlePlayerCollisions(Player local, List<Player> remotes) {
        if (remotes == null) return;

        Car localCar = local.getCar();

        for (Player remote : remotes) {
            if (remote == null || remote.getCar() == null) continue;

            Car remoteCar = remote.getCar();

            if (localCar.collidesWith(remoteCar)) {


                resolveCollision(localCar);
                resolveCollision(remoteCar);

                if (canApplyCollision()) {
                    rules.handlePlayersCollision(local, remote);
                }
            }
        }
    }

    // ======================
    // RESOLVER COLISIÓN
    // ======================
    private void resolveCollision(Car car) {
        car.undoLastMove();
    }

    // ======================
    // CONTROL DE COLISIONES
    // ======================
    private boolean canApplyCollision() {
        long now = System.currentTimeMillis();

        if (now - lastCollisionTime > collisionCooldown) {
            lastCollisionTime = now;
            return true;
        }
        return false;
    }

    private boolean isValid(Player player) {
        return player != null && player.getCar() != null;
    }
}