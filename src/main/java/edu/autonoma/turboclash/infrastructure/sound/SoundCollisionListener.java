package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Representa la clase `SoundCollisionListener` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class SoundCollisionListener implements CollisionListener {

    @Override
    /**
     * Ejecuta la operacion publica `onItemCollision`.
     * @param player valor del parametro `player`
     */
    public void onItemCollision(Player player) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.POINT);
    }

    @Override
    /**
     * Ejecuta la operacion publica `onObstacleCollision`.
     * @param player valor del parametro `player`
     */
    public void onObstacleCollision(Player player) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.BRAKE);
    }

    @Override
    /**
     * Ejecuta la operacion publica `onPlayersCollision`.
     * @param p1 valor del parametro `p1`
     * @param p2 valor del parametro `p2`
     */
    public void onPlayersCollision(Player p1, Player p2) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.COLLISION);
    }
}
