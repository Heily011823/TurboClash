package edu.autonoma.turboclash.config;

import edu.autonoma.turboclash.domain.model.ObstacleType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la responsabilidad de {@code GameConfig} en la configuracion del juego.
 */
public class GameConfig {

    private final int minPort;
    private final int maxPort;
    private final int frameDelay;
    private final int initialLives;
    private final int targetScore;
    private final long collisionCooldown;
    private final double carStartX;
    private final double carStartY;

    private final List<Point> itemSpawnPoints;
    private final List<SpawnPoint> obstacleSpawnPoints;

    /**
     * Crea una nueva instancia de {@code GameConfig}.
     */
    public GameConfig() {
        this.minPort = 5000;
        this.maxPort = 5003;
        this.frameDelay = 16;
        this.initialLives = 3;
        this.targetScore = 10;
        this.collisionCooldown = 1000;
        this.carStartX = 50.0;
        this.carStartY = 300.0;


        this.itemSpawnPoints = new ArrayList<>();
        this.itemSpawnPoints.add(new Point(850, 150));
        this.itemSpawnPoints.add(new Point(850, 300));
        this.itemSpawnPoints.add(new Point(850, 450));


        this.obstacleSpawnPoints = new ArrayList<>();
        this.obstacleSpawnPoints.add(new SpawnPoint(1000, 200, ObstacleType.OIL));
        this.obstacleSpawnPoints.add(new SpawnPoint(1200, 400, ObstacleType.CONE));
        this.obstacleSpawnPoints.add(new SpawnPoint(1400, 100, ObstacleType.BARRIER));
    }

    public int getMinPort() { return minPort; }
    public int getMaxPort() { return maxPort; }
    public int getFrameDelay() { return frameDelay; }

    public int getTargetScore() { return targetScore; }
    public long getCollisionCooldown() { return collisionCooldown; }


    /**
     * Indica si {@code ValidPort}.
     *
     * @param puerto valor del parametro {@code puerto}
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isValidPort(int puerto) {
        return puerto >= minPort && puerto <= maxPort;
    }
}
