package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa la clase `CollisionManager` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class CollisionManager {

    private static final long PLAYER_COLLISION_COOLDOWN_MS = 700L;
    private final CollisionListener listener;
    private final Map<String, Long> playerCollisionCooldowns = new HashMap<>();

    /**
     * Crea una nueva instancia de `CollisionManager`.
     * @param listener valor del parametro `listener`
     */
    public CollisionManager(CollisionListener listener) {
        this.listener = listener;
    }

    /**
     * Ejecuta la operacion publica `process`.
     * @param match valor del parametro `match`
     * @param items valor del parametro `items`
     * @param obstacles valor del parametro `obstacles`
     */
    public void process(Match match,
                        List<Item> items,
                        List<Obstacle> obstacles) {

        if (match == null) return;

        for (Player player : match.getPlayers()) {
            if (!canCollide(player)) {
                continue;
            }

            checkCarCollisions(player, obstacles);
            checkItemCollisions(player, items);
        }
        checkPlayerCollisions(match.getPlayers());
    }

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

    private void checkPlayerCollisions(List<Player> players) {
        if (players == null || players.size() < 2) {
            return;
        }

        long now = System.currentTimeMillis();

        for (int i = 0; i < players.size(); i++) {
            Player first = players.get(i);
            if (!canCollide(first)) {
                continue;
            }

            for (int j = i + 1; j < players.size(); j++) {
                Player second = players.get(j);
                if (!canCollide(second)) {
                    continue;
                }

                if (!first.getCar().getBounds().intersects(second.getCar().getBounds())) {
                    continue;
                }

                String key = buildPairKey(first, second);
                long lastCollisionTime = playerCollisionCooldowns.getOrDefault(key, 0L);
                if (now - lastCollisionTime < PLAYER_COLLISION_COOLDOWN_MS) {
                    continue;
                }

                playerCollisionCooldowns.put(key, now);
                listener.onPlayersCollision(first, second);
            }
        }
    }

    private boolean canCollide(Player player) {
        return player != null
                && player.getCar() != null
                && player.getCar().isActive()
                && !player.isEliminated();
    }

    private String buildPairKey(Player first, Player second) {
        String id1 = first.getId() != null ? first.getId() : first.getName();
        String id2 = second.getId() != null ? second.getId() : second.getName();
        return id1.compareTo(id2) <= 0 ? id1 + "|" + id2 : id2 + "|" + id1;
    }
}
