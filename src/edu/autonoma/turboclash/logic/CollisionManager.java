package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;

import java.util.List;

public class CollisionManager {

    public void process(Player player, List<Item> items, List<Obstacle> obstacles) {
        if (player == null || player.getCar() == null) return;

        for (Item item : items) {
            if (player.getCar().collidesWith(item)) {
                item.applyEffect(player);
            }
        }

        for (Obstacle obstacle : obstacles) {
            if (player.getCar().collidesWith(obstacle)) {
                obstacle.affectPlayer(player);
            }
        }
    }
}