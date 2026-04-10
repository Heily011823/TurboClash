package edu.autonoma.turboclash.config;

import edu.autonoma.turboclash.domain.model.ObstacleType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la clase `GameConfig` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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
     * Crea una nueva instancia de `GameConfig`.
     */
    public GameConfig() {
        this.minPort = 5001;
        this.maxPort = 5004;
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

    /**
     * Obtiene el valor asociado a `getMinPort`.
     * @return resultado de la operacion documentada
     */
    public int getMinPort() { return minPort; }
    /**
     * Obtiene el valor asociado a `getMaxPort`.
     * @return resultado de la operacion documentada
     */
    public int getMaxPort() { return maxPort; }
    /**
     * Obtiene el valor asociado a `getFrameDelay`.
     * @return resultado de la operacion documentada
     */
    public int getFrameDelay() { return frameDelay; }

    /**
     * Obtiene el valor asociado a `getTargetScore`.
     * @return resultado de la operacion documentada
     */
    public int getTargetScore() { return targetScore; }
    /**
     * Obtiene el valor asociado a `getCollisionCooldown`.
     * @return resultado de la operacion documentada
     */
    public long getCollisionCooldown() { return collisionCooldown; }


    /**
     * Indica la condicion evaluada por `isValidPort`.
     * @param puerto valor del parametro `puerto`
     * @return resultado de la operacion documentada
     */
    public boolean isValidPort(int puerto) {
        return puerto >= minPort && puerto <= maxPort;
    }
}
