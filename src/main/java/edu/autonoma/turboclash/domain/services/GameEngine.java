package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.*;

import java.util.List;

/**
 * Representa la clase `GameEngine` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;

    private final List<Item> items;
    private final List<Obstacle> obstacles;

    private static final double WORLD_SPEED = 6.0;

    /**
     * Crea una nueva instancia de `GameEngine`.
     * @param match valor del parametro `match`
     * @param collisionManager valor del parametro `collisionManager`
     * @param items valor del parametro `items`
     * @param obstacles valor del parametro `obstacles`
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
     * Ejecuta la operacion publica `update`.
     */
    public void update() {
        if (match.isFinished()) return;

        updatePlayers();
        updateWorld();
        processCollisions();
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
        collisionManager.process(match, items, obstacles);
    }

    /**
     * Obtiene el valor asociado a `getItems`.
     * @return resultado de la operacion documentada
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Obtiene el valor asociado a `getObstacles`.
     * @return resultado de la operacion documentada
     */
    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}
