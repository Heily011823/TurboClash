package edu.autonoma.turboclash.model;

import edu.autonoma.turboclash.logic.GameConstants;

/**
 * Car: Pure data model.
 * Responsibilities: State, position, and logic. No Rendering.
 */
public class Car extends GameObject {
    private int lives = GameConstants.INITIAL_LIVES;
    private double speedMultiplier = 1.0;
    private long penaltyEndTime = 0;
    private boolean active = true;
    private boolean finishReached = false;
    private double lastDx, lastDy;
    private String carColor;

    public Car(String id, double x, double y, String carColor) {

        super(id, x, y, GameConstants.CAR_WIDTH, GameConstants.CAR_HEIGHT);
        this.carColor = carColor;
    }


    public void move(double dx, double dy) {
        if (!active) return;
        this.lastDx = dx * speedMultiplier;
        this.lastDy = dy * speedMultiplier;
        this.posX += lastDx;
        this.posY += lastDy;
    }

    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;
    }

    public void reduceLife() {
        if (lives > 0) {
            lives--;
            if (lives <= 0) active = false;
        }
    }

    public void applyDebuff(double factor, long duration) {
        this.speedMultiplier = factor;
        this.penaltyEndTime = System.currentTimeMillis() + duration;
    }


    public void update() {
        if (penaltyEndTime > 0 && System.currentTimeMillis() > penaltyEndTime) {
            speedMultiplier = 1.0;
            penaltyEndTime = 0;
        }
    }


    public void setLives(int lives) {
        this.lives = lives;
        this.active = (this.lives > 0);
    }

    public int getLives() { return lives; }
    public boolean isActive() { return active; }
    public String getCarColor() { return carColor; }
    public void setFinishReached(boolean reached) { this.finishReached = reached; }
    public boolean isFinishReached() { return finishReached; }
}