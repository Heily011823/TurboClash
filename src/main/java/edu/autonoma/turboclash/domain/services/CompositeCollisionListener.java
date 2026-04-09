package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Permite ejecutar varios listeners de colisión al mismo tiempo.
 * Así se aplican las reglas del juego y también los efectos de sonido.
 */
public class CompositeCollisionListener implements CollisionListener {

    private final CollisionListener[] listeners;

    public CompositeCollisionListener(CollisionListener... listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onItemCollision(Player player) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onItemCollision(player);
            }
        }
    }

    @Override
    public void onObstacleCollision(Player player) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onObstacleCollision(player);
            }
        }
    }

    @Override
    public void onPlayersCollision(Player p1, Player p2) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onPlayersCollision(p1, p2);
            }
        }
    }
}