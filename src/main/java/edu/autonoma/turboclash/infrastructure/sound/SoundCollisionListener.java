package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.GameObject;

public class SoundCollisionListener implements CollisionListener {

    @Override
    public void onCollision(GameObject object) {
        SoundManager.getInstance()
                .playEffect(SoundManager.Sound.BRAKE);
    }
}