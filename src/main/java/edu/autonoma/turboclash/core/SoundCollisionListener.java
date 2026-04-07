package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.sound.SoundManager;

public class SoundCollisionListener implements CollisionListener {

    @Override
    public void onCollision(Obstacle obstacle) {
        SoundManager.getInstance()
                .playEffect(SoundManager.Sound.BRAKE);
    }
}