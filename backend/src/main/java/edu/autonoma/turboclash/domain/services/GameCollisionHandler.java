package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Representa la clase `GameCollisionHandler` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameCollisionHandler implements CollisionListener {

    private final GameRulesManager rules;

    /**
     * Crea una nueva instancia de `GameCollisionHandler`.
     * @param rules valor del parametro `rules`
     */
    public GameCollisionHandler(GameRulesManager rules) {
        this.rules = rules;
    }

    @Override
    /**
     * Ejecuta la operacion publica `onItemCollision`.
     * @param player valor del parametro `player`
     */
    public void onItemCollision(Player player) {
        rules.applyCoinReward(player);
    }

    @Override
    /**
     * Ejecuta la operacion publica `onObstacleCollision`.
     * @param player valor del parametro `player`
     */
    public void onObstacleCollision(Player player) {
        rules.applyObstaclePenalty(player);
    }

    @Override
    /**
     * Ejecuta la operacion publica `onPlayersCollision`.
     * @param p1 valor del parametro `p1`
     * @param p2 valor del parametro `p2`
     */
    public void onPlayersCollision(Player p1, Player p2) {
        rules.handlePlayersCollision(p1, p2);
    }
}
