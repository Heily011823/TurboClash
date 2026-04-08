package edu.autonoma.turboclash.domain.events;

import edu.autonoma.turboclash.domain.model.Obstacle;

public interface CollisionListener {
    void onCollision(Obstacle obstacle);
}

