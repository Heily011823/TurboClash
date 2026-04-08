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
     * Procesa todas las colisiones del juego.
     */
    public void process(Match match,
                        List<Item> items,
                        List<Obstacle> obstacles) {

        if (match == null) return;


        checkCarCollisions(match.getLocalPlayer(), obstacles);
        checkItemCollisions(match.getLocalPlayer(), items);


        checkRemoteCars(match.getRemotePlayers(), obstacles);


        checkPlayerVsPlayer(match.getPlayers());
    }

    /**
     * Colisiones contra obstáculos.
     */
    private void checkCarCollisions(Player player, List<Obstacle> obstacles) {
        if (player == null || player.getCar() == null) return;

        for (Obstacle obs : obstacles) {
            if (!obs.isProcessed() &&
                    player.getCar().getBounds().intersects(obs.getBounds())) {

                obs.setProcessed(true);


                listener.onObstacleCollision(player);
            }
        }
    }

    /**
     * Aplica colisiones a jugadores remotos.
     */
    private void checkRemoteCars(List<Player> players, List<Obstacle> obstacles) {
        if (players == null) return;

        for (Player p : players) {
            checkCarCollisions(p, obstacles);
        }
    }

    /**
     * Colisiones con items.
     */
    private void checkItemCollisions(Player player, List<Item> items) {
        if (player == null || player.getCar() == null) return;

        for (Item item : items) {
            if (item.isVisible() &&
                    player.getCar().getBounds().intersects(item.getBounds())) {

                item.setVisible(false);


                listener.onItemCollision(player);
            }
        }
    }

    /**
     * Colisiones entre jugadores.
     */
    private void checkPlayerVsPlayer(List<Player> players) {
        if (players == null) return;

        for (int i = 0; i < players.size(); i++) {
            Player p1 = players.get(i);

            if (p1 == null || p1.getCar() == null) continue;

            for (int j = i + 1; j < players.size(); j++) {
                Player p2 = players.get(j);

                if (p2 == null || p2.getCar() == null) continue;

                if (p1.getCar().getBounds().intersects(p2.getCar().getBounds())) {


                    listener.onPlayersCollision(p1, p2);
                }
            }
        }
    }
}