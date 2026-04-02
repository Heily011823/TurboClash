package edu.autonoma.turboclash.model;

import edu.autonoma.turboclash.logic.GameConstants;
import java.awt.Graphics;

public class Car extends GameObject {

    private boolean active;
    private int lives;
    private double speedMultiplier;
    private double baseSpeedMultiplier;
    private long speedPenaltyEndTime;
    private boolean finishLineReached;

    public Car(String id, double x, double y, int width, int height) {
        super(id, x, y, width, height);
        this.active = true;
        this.lives = GameConstants.INITIAL_LIVES;
        this.speedMultiplier = 1.0;
        this.baseSpeedMultiplier = 1.0;
        this.speedPenaltyEndTime = 0;
        this.finishLineReached = false;
    }

    @Override
    public void draw(Graphics g) {
        if (!isVisible()) return;
        g.fillRect((int) posX, (int) posY, width, height);
    }

    public void moveTo(double x, double y) {
        if (!active) return;
        this.posX = x;
        this.posY = y;
    }

    public void reduceLife() {
        if (lives <= 0) return;

        lives--;

        if (lives == 0) {
            active = false;
        }
    }


    public void applySpeedPenalty(double factor, long durationMs) {
        this.speedMultiplier = factor;
        this.speedPenaltyEndTime = System.currentTimeMillis() + durationMs;
    }


    public void applySpeedModifier(double factor, long durationMs) {
        applySpeedPenalty(factor, durationMs);
    }

    public void update() {
        if (speedPenaltyEndTime > 0 &&
                System.currentTimeMillis() > speedPenaltyEndTime) {

            speedMultiplier = baseSpeedMultiplier;
            speedPenaltyEndTime = 0;
        }
    }

    // META
    public void setFinishLineReached(boolean reached) {
        this.finishLineReached = reached;
    }

    public boolean hasReachedFinishLine() {
        return finishLineReached;
    }

    // GETTERS
    public int getLives() {
        return lives;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean isActive() {
        return active;
    }

    public void reset() {
        this.lives = GameConstants.INITIAL_LIVES;
        this.active = true;
        this.speedMultiplier = baseSpeedMultiplier;
        this.speedPenaltyEndTime = 0;
        this.finishLineReached = false;
    }
}