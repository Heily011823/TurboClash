package edu.autonoma.turboclash.model;

/**
 * Obstacle: Represents hazards on the track.
 * Pure model class: images are handled by the View layer.
 */
public class Obstacle extends GameObject {

    private final String type; // e.g., "OIL", "BARRIER", "CONE"
    private boolean processed = false;

    public Obstacle(String id, double x, double y, int width, int height, String type) {
        super(id, x, y, width, height);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}