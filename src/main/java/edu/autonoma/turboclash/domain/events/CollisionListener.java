package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.GameObject;

/**
 * Define el contrato de escucha y coordinacion para {@code CollisionListener} para la gestion de eventos del dominio.
 */
public interface CollisionListener {
    /**
     * Atiende {@code Collision}.
     *
     * @param object valor del parametro {@code object}
     */
    void onCollision(GameObject object);
}
