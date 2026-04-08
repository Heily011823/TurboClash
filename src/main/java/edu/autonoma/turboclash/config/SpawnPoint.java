package edu.autonoma.turboclash.config;

import edu.autonoma.turboclash.domain.model.ObstacleType;

public class SpawnPoint {

    private final int x;
    private final int y;
    private final ObstacleType type;

    public SpawnPoint(int x, int y, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ObstacleType getType() {
        return type;
    }
}