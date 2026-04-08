package edu.autonoma.turboclash.infrastructure.sound;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

public class SoundCollisionListener implements CollisionListener {

    @Override
    public void onItemCollision(Player player) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.POINT);
    }

    @Override
    public void onObstacleCollision(Player player) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.BRAKE);
    }

    @Override
    public void onPlayersCollision(Player p1, Player p2) {
        SoundManager.getInstance().playEffect(SoundManager.Sound.COLLISION);
    }
}