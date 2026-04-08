package edu.autonoma.turboclash.domain.model;

public class Obstacle extends GameObject {


    private final ObstacleType type;

    private boolean processed = false;

    public Obstacle(String id, double x, double y, int width, int height, ObstacleType type) {
        super(id, x, y, width, height);
        this.type = type;
    }

    public ObstacleType getType() {
        return type;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getImage() {
        return type.getImage();
    }
}