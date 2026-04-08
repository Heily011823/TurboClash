package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.ObstacleType;
import edu.autonoma.turboclash.presentation.view.GameViewport;

import java.util.List;
import java.util.Random;


/**
 * Representa la responsabilidad de {@code GameSpawner} en los servicios de dominio.
 */
public class GameSpawner implements Runnable {

    private final List<Item> items;
    private final List<Obstacle> obstacles;
    private final Random random;
    private boolean running;


    private static final int SPAWN_DELAY_MS = 1500;

    /**
     * Crea una nueva instancia de {@code GameSpawner}.
     *
     * @param items valor del parametro {@code items}
     * @param obstacles valor del parametro {@code obstacles}
     */
    public GameSpawner(List<Item> items, List<Obstacle> obstacles) {
        this.items = items;
        this.obstacles = obstacles;
        this.random = new Random();
        this.running = false;
    }

    /**
     * Inicia la operacion principal del metodo.
     */
    public void start() {
        if (!running) {
            running = true;
            Thread t = new Thread(this, "SpawnerThread");
            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * Detiene la operacion principal del metodo.
     */
    public void stop() {
        running = false;
    }

    @Override
    /**
     * Ejecuta la tarea principal de {@code GameSpawner}.
     */
    public void run() {
        while (running) {
            try {
                Thread.sleep(SPAWN_DELAY_MS);

                int chance = random.nextInt(10);

                if (chance < 3) {
                    spawnItem();
                } else {
                    spawnObstacle();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Ejecuta la operacion {@code spawnItem}.
     */
    private void spawnItem() {
        double x = GameViewport.ITEM_SPAWN_X;
        double y = GameViewport.randomPlayableY(30, random);

        String id = "coin_" + System.currentTimeMillis();
        items.add(new Item(id, x, y, 30, 30));
    }
    /**
     * Ejecuta la operacion {@code spawnObstacle}.
     */
    private void spawnObstacle() {
        double x = GameViewport.OBSTACLE_SPAWN_X;
        double y = GameViewport.randomPlayableY(45, random);


        ObstacleType[] types = ObstacleType.values();
        ObstacleType selectedType = types[random.nextInt(types.length)];

        String id = "obs_" + System.currentTimeMillis();


        obstacles.add(new Obstacle(id, x, y, 45, 45, selectedType));
    }
}
