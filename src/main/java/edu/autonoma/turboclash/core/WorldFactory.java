package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.model.ObstacleType;

import java.util.List;
import java.util.UUID;

public class WorldFactory {
    public List<Item> createItems() {
        return List.of(
                new Item(UUID.randomUUID().toString(), 400, 300, 25, 25),
                new Item(UUID.randomUUID().toString(), 600, 150, 25, 25)
        );
    }

    public List<Obstacle> createObstacles() {
        return List.of(
                new Obstacle(UUID.randomUUID().toString(), 350, 250, 50, 50, ObstacleType.OIL)
        );
    }
}
