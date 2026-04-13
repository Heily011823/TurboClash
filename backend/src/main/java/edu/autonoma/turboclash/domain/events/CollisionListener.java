package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.GameObject;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Define el contrato de `CollisionListener` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public interface CollisionListener {

    void onItemCollision(Player player);

    void onObstacleCollision(Player player);

    void onPlayersCollision(Player p1, Player p2);
}
