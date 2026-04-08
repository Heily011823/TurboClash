package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.*;

import java.util.List;

/**
 * Administra la responsabilidad principal de {@code CollisionManager} en los servicios de dominio.
 */
public class CollisionManager {

    private final CollisionListener listener;

    /**
     * Crea una nueva instancia de {@code CollisionManager}.
     *
     * @param listener valor del parametro {@code listener}
     */
    public CollisionManager(CollisionListener listener) {
        this.listener = listener;
    }

    /**
     * Ejecuta la operacion {@code process}.
     *
     * @param match valor del parametro {@code match}
     * @param items valor del parametro {@code items}
     * @param obstacles valor del parametro {@code obstacles}
     */
    public void process(Match match,
                        List<Item> items,
                        List<Obstacle> obstacles) {

        checkCarCollisions(match.getLocalPlayer(), obstacles);
        checkRemoteCars(match.getRemotePlayers(), obstacles);
        checkItemCollisions(match.getLocalPlayer(), items);
    }

    /**
     * Ejecuta la operacion {@code checkCarCollisions}.
     *
     * @param player valor del parametro {@code player}
     * @param obstacles valor del parametro {@code obstacles}
     */
    private void checkCarCollisions(Player player, List<Obstacle> obstacles) {
        if (player == null || player.getCar() == null) return;

        for (Obstacle obs : obstacles) {
            if (!obs.isProcessed() &&
                    player.getCar().getBounds().intersects(obs.getBounds())) {

                obs.setProcessed(true);
                listener.onCollision(obs);
            }
        }
    }

    /**
     * Ejecuta la operacion {@code checkRemoteCars}.
     *
     * @param players valor del parametro {@code players}
     * @param obstacles valor del parametro {@code obstacles}
     */
    private void checkRemoteCars(List<Player> players, List<Obstacle> obstacles) {
        for (Player p : players) {
            checkCarCollisions(p, obstacles);
        }
    }

    /**
     * Ejecuta la operacion {@code checkItemCollisions}.
     *
     * @param player valor del parametro {@code player}
     * @param items valor del parametro {@code items}
     */
    private void checkItemCollisions(Player player, List<Item> items) {
        if (player == null || player.getCar() == null) return;

        for (Item item : items) {
            if (item.isVisible() &&
                    player.getCar().getBounds().intersects(item.getBounds())) {

                item.setVisible(false);
                listener.onCollision(item);
            }
        }
    }
}
