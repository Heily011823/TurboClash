package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Obstacle;

public interface CollisionListener {
    void onCollision(Obstacle obstacle);
}

