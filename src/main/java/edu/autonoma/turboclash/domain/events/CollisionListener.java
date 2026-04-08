package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.GameObject;
import edu.autonoma.turboclash.domain.model.Player;

/**
 * Define el contrato de escucha y coordinacion para {@code CollisionListener} para la gestion de eventos del dominio.
 */
public interface CollisionListener {

    void onItemCollision(Player player);

    void onObstacleCollision(Player player);

    void onPlayersCollision(Player p1, Player p2);
}