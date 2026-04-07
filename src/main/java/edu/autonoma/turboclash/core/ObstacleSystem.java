package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.sound.SoundManager;

import java.util.List;

public class ObstacleSystem {

    public void check(Car car, List<Obstacle> obstacles) {

        for (Obstacle obs : obstacles) {
            if (!obs.isProcessed() &&
                    car.getBounds().intersects(obs.getBounds())) {

                obs.setProcessed(true);

                SoundManager.getInstance()
                        .playEffect(SoundManager.Sound.BRAKE);
            }
        }
    }
}
