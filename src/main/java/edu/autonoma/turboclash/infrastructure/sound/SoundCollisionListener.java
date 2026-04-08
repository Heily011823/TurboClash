package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Obstacle;

public class SoundCollisionListener implements CollisionListener {

    @Override
    public void onCollision(Obstacle obstacle) {
        SoundManager.getInstance()
                .playEffect(SoundManager.Sound.BRAKE);
    }
}