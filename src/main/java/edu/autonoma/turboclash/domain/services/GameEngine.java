package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.*;

import java.util.List;

/**
 * Representa la responsabilidad de {@code GameEngine} en los servicios de dominio.
 */
public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;

    private final List<Item> items;
    private final List<Obstacle> obstacles;

    private static final double WORLD_SPEED = 6.0;
    private static final double DEAD_ZONE_X = 0;

    /**
     * Crea una nueva instancia de {@code GameEngine}.
     *
     * @param match valor del parametro {@code match}
     * @param collisionManager valor del parametro {@code collisionManager}
     * @param items valor del parametro {@code items}
     * @param obstacles valor del parametro {@code obstacles}
     */
    public GameEngine(Match match,
                      CollisionManager collisionManager,
                      List<Item> items,
                      List<Obstacle> obstacles) {

        this.match = match;
        this.collisionManager = collisionManager;
        this.items = items;
        this.obstacles = obstacles;
    }

    /**
     * Actualiza la operacion principal del metodo.
     */
    public void update() {
        if (match.isFinished()) return;

        updatePlayers();
        updateWorld();
        processCollisions();
        checkPlayerOut();

        match.check();
    }

    /**
     * Actualiza {@code Players}.
     */
    private void updatePlayers() {
        for (Player p : match.getPlayers()) {
            if (p.getCar() != null) {
                p.getCar().updateDebuff();
            }
        }
    }

    /**
     * Actualiza {@code World}.
     */
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

    /**
     * Ejecuta la operacion {@code processCollisions}.
     */
    private void processCollisions() {
        collisionManager.process(match, items, obstacles);
    }

    /**
     * Ejecuta la operacion {@code checkPlayerOut}.
     */
    private void checkPlayerOut() {
        Player local = match.getLocalPlayer();

        if (local == null || local.getCar() == null) return;

        if (local.getCar().getX() <= DEAD_ZONE_X) {
            match.setFinished(null);
        }
    }

    /**
     * Obtiene el valor de {@code Items}.
     *
     * @return valor de {@code Items}
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Obtiene el valor de {@code Obstacles}.
     *
     * @return valor de {@code Obstacles}
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}
