package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;

import java.util.List;
import java.util.UUID;

public class WorldFactory {

    private final GameConfig config;

    public WorldFactory(GameConfig config) {
        this.config = config;
    }

    public List<Item> createItems() {
        return config.getItemSpawnPoints().stream()
                .map(p -> new Item(
                        UUID.randomUUID().toString(),
                        p.x,
                        p.y,
                        25, 25
                ))
                .toList();
    }

    public List<Obstacle> createObstacles() {
        return config.getObstacleSpawnPoints().stream()
                .map(p -> new Obstacle(
                        UUID.randomUUID().toString(),
                        p.getX(),
                        p.getY(),
                        50, 50,
                        p.getType()
                ))
                .toList();
    }
}