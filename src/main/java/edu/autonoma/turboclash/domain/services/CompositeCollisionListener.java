package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Representa la clase `CompositeCollisionListener` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class CompositeCollisionListener implements CollisionListener {

    private final CollisionListener[] listeners;

    /**
     * Crea una nueva instancia de `CompositeCollisionListener`.
     * @param listeners valor del parametro `listeners`
     */
    public CompositeCollisionListener(CollisionListener... listeners) {
        this.listeners = listeners;
    }

    @Override
    /**
     * Ejecuta la operacion publica `onItemCollision`.
     * @param player valor del parametro `player`
     */
    public void onItemCollision(Player player) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onItemCollision(player);
            }
        }
    }

    @Override
    /**
     * Ejecuta la operacion publica `onObstacleCollision`.
     * @param player valor del parametro `player`
     */
    public void onObstacleCollision(Player player) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onObstacleCollision(player);
            }
        }
    }

    @Override
    /**
     * Ejecuta la operacion publica `onPlayersCollision`.
     * @param p1 valor del parametro `p1`
     * @param p2 valor del parametro `p2`
     */
    public void onPlayersCollision(Player p1, Player p2) {
        if (listeners == null) return;

        for (CollisionListener listener : listeners) {
            if (listener != null) {
                listener.onPlayersCollision(p1, p2);
            }
        }
    }
}
