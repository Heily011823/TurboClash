package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.ObstacleType;
import edu.autonoma.turboclash.presentation.view.GameViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Crea y configura instancias relacionadas con {@code WorldFactory} en la infraestructura del sistema.
 */
public class WorldFactory {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de {@code WorldFactory}.
     *
     * @param config valor del parametro {@code config}
     */
    public WorldFactory(GameConfig config) {
        this.config = config;
    }

    /**
     * Crea {@code Items}.
     *
     * @return instancia creada para {@code Items}
     */
    public List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.35, GameViewport.laneY(1), 25, 25));
        items.add(new Item(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.55, GameViewport.laneY(0), 25, 25));
        return items;
    }

    /**
     * Crea {@code Obstacles}.
     *
     * @return instancia creada para {@code Obstacles}
     */
    public List<Obstacle> createObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.30, GameViewport.laneY(1), 50, 50, ObstacleType.OIL));
        return obstacles;
    }
}
