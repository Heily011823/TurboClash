package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.GameObject;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Define el contrato de `CollisionListener` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface CollisionListener {

    void onItemCollision(Player player);

    void onObstacleCollision(Player player);

    void onPlayersCollision(Player p1, Player p2);
}
