package edu.autonoma.turboclash.model;

/**
 * Represents a vehicle controlled by a player.
 *
 * Design Principles Applied:
 * - SRP: Handles only vehicle state and movement logic.
 * - Encapsulation: Protects internal state with controlled access.
 * - DRY: Reuses collision logic from GameObject.
 */
public class Car extends GameObject {

    private final String model;
    private final String color;
    private double speed;
    private boolean active;

    // Fixed Initial State (to avoid magic numbers and hard-coded resets)
    private final double spawnX;
    private final double spawnY;
    private static final double MIN_SPEED = 0.0;

    public Car(String id, String model, String color,
               double posX, double posY,
               int width, int height) {

        super(id, posX, posY, width, height);
        this.model = model;
        this.color = color;
        this.spawnX = posX; // Store the original position
        this.spawnY = posY;
        this.speed = MIN_SPEED;
        this.active = true;
    }

    /**
     * Moves the car to a new position if it is active.
     */
    public void moveTo(double x, double y) {
        if (active) {
            this.posX = x;
            this.posY = y;
        }
    }

    /**
     * Resets the car to its specific initial spawn point.
     */
    public void resetPosition() {
        this.posX = this.spawnX;
        this.posY = this.spawnY;
        this.speed = MIN_SPEED;
    }

    /**
     * Increases speed safely.
     */
    public void accelerate(double amount) {
        if (amount > 0 && active) {
            this.speed += amount;
        }
    }

    /**
     * Reduces speed without going below zero.
     */
    public void brake(double amount) {
        if (amount > 0) {
            this.speed = Math.max(MIN_SPEED, this.speed - amount);
        }
    }

    /**
     * Stops the car completely.
     */
    public void stop() {
        this.speed = MIN_SPEED;
    }

    // Getters

    public String getModel() { return model; }
    public String getColor() { return color; }
    public double getSpeed() { return speed; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}