package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

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
     * Procesa solo las colisiones del jugador local.
     */
    public void process(Match match,
                        List<Item> items,
                        List<Obstacle> obstacles) {

        if (match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null || localPlayer.getCar() == null) return;

        checkCarCollisions(localPlayer, obstacles);
        checkItemCollisions(localPlayer, items);
    }

    /**
     * Colisiones contra obstáculos.
     */
    private void checkCarCollisions(Player player, List<Obstacle> obstacles) {
        if (player == null || player.getCar() == null || obstacles == null) return;

        for (Obstacle obs : obstacles) {
            if (!obs.isProcessed() &&
                    player.getCar().getBounds().intersects(obs.getBounds())) {

                obs.setProcessed(true);
                listener.onObstacleCollision(player);
            }
        }
    }

    /**
     * Colisiones con items.
     */
    private void checkItemCollisions(Player player, List<Item> items) {
        if (player == null || player.getCar() == null || items == null) return;

        for (Item item : items) {
            if (item.isVisible() &&
                    player.getCar().getBounds().intersects(item.getBounds())) {

                item.setVisible(false);
                listener.onItemCollision(player);
            }
        }
    }
}