package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Obstacle;

import java.util.List;

public class ObstacleSystem {

    private final CollisionListener listener;

    public ObstacleSystem(CollisionListener listener) {
        this.listener = listener;
    }

    public void check(Car car, List<Obstacle> obstacles) {

        for (Obstacle obs : obstacles) {
            if (!obs.isProcessed() &&
                    car.getBounds().intersects(obs.getBounds())) {

                obs.setProcessed(true);
                listener.onCollision(obs);
            }
        }
    }
}