package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.GameObject;

public interface CollisionListener {
    void onCollision(GameObject object);
}