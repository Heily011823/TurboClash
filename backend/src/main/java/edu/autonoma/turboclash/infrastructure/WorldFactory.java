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
 * Representa la clase `WorldFactory` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class WorldFactory {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de `WorldFactory`.
     * @param config valor del parametro `config`
     */
    public WorldFactory(GameConfig config) {
        this.config = config;
    }

    /**
     * Crea el recurso necesario para create items.
     * @return resultado de la operacion documentada
     */
    public List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.35, GameViewport.laneY(1), 25, 25));
        items.add(new Item(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.55, GameViewport.laneY(0), 25, 25));
        return items;
    }

    /**
     * Crea el recurso necesario para create obstacles.
     * @return resultado de la operacion documentada
     */
    public List<Obstacle> createObstacles() {
        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(UUID.randomUUID().toString(), GameViewport.WIDTH * 0.30, GameViewport.laneY(1), 50, 50, ObstacleType.OIL));
        return obstacles;
    }
}
