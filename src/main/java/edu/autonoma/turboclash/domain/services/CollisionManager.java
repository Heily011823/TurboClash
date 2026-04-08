package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.*;

import java.util.List;

public class CollisionManager {

    private final CollisionListener listener;

    public CollisionManager(CollisionListener listener) {
        this.listener = listener;
    }

    public void process(Match match,
                        List<Item> items,
                        List<Obstacle> obstacles) {

        checkCarCollisions(match.getLocalPlayer(), obstacles);
        checkRemoteCars(match.getRemotePlayers(), obstacles);
        checkItemCollisions(match.getLocalPlayer(), items);
    }

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

    private void checkRemoteCars(List<Player> players, List<Obstacle> obstacles) {
        for (Player p : players) {
            checkCarCollisions(p, obstacles);
        }
    }

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