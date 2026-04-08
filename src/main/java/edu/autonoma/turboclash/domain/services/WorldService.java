package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;

import java.util.List;

public class WorldService {

    private static final double WORLD_SPEED = 6.0;

    public void updateWorld(List<Item> items, List<Obstacle> obstacles) {

        for (Item item : items) {
            item.setPosition(item.getX() - WORLD_SPEED, item.getY());
        }

        for (Obstacle obs : obstacles) {
            obs.setPosition(obs.getX() - WORLD_SPEED, obs.getY());
        }

        items.removeIf(item -> item.getX() < -100);
        obstacles.removeIf(obs -> obs.getX() < -100);
    }
}
