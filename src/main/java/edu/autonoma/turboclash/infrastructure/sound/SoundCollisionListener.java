package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.GameObject;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;

/**
 * Representa la responsabilidad de {@code SoundCollisionListener} en la infraestructura de audio.
 */
public class SoundCollisionListener implements CollisionListener {

    @Override
    /**
     * Atiende {@code Collision}.
     *
     * @param object valor del parametro {@code object}
     */
    public void onCollision(GameObject object) {
        if (object instanceof Item) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.POINT);
        } else if (object instanceof Obstacle) {
            SoundManager.getInstance().playEffect(SoundManager.Sound.BRAKE);
        }
    }
}
