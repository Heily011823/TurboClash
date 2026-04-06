package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.model.ObstacleType;

import java.util.List;
import java.util.Random;


public class GameSpawner implements Runnable {

    private final List<Item> items;
    private final List<Obstacle> obstacles;
    private final Random random;
    private boolean running;


    private static final int SPAWN_DELAY_MS = 1500;

    public GameSpawner(List<Item> items, List<Obstacle> obstacles) {
        this.items = items;
        this.obstacles = obstacles;
        this.random = new Random();
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            Thread t = new Thread(this, "SpawnerThread");
            t.setDaemon(true);
            t.start();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
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

    private void spawnItem() {

        double x = 850;
        double y = 100 + random.nextInt(400);

        String id = "coin_" + System.currentTimeMillis();
        items.add(new Item(id, x, y, 30, 30));
    }
    private void spawnObstacle() {
        double x = 850;
        double y = 100 + random.nextInt(400);


        ObstacleType[] types = ObstacleType.values();
        ObstacleType selectedType = types[random.nextInt(types.length)];

        String id = "obs_" + System.currentTimeMillis();


        obstacles.add(new Obstacle(id, x, y, 45, 45, selectedType));
    }
}