package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.GameObject;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;

public class SoundCollisionListener implements CollisionListener {

    @Override
    public void onCollision(GameObject object) {
        if (object instanceof Item) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.POINT);
        } else if (object instanceof Obstacle) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.BRAKE);
        }
    }
}