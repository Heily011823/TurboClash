package edu.autonoma.turboclash.model;

import edu.autonoma.turboclash.logic.GameConstants;
import java.awt.Graphics;
import java.awt.Color;

public class Car extends GameObject {

    private boolean active;
    private int lives;
    private double speedMultiplier;
    private double baseSpeedMultiplier;
    private long speedPenaltyEndTime;
    private boolean finishLineReached;


    private double lastDx;
    private double lastDy;

    public Car(String id, double x, double y, int width, int height) {
        super(id, x, y, width, height);
        this.active = true;
        this.lives = GameConstants.INITIAL_LIVES;
        this.speedMultiplier = 1.0;
        this.baseSpeedMultiplier = 1.0;
        this.speedPenaltyEndTime = 0;
        this.finishLineReached = false;
        this.lastDx = 0;
        this.lastDy = 0;
    }

    @Override
    public void draw(Graphics g) {
        if (!isVisible()) return;


        g.setColor(Color.BLUE);
        g.fillRect((int) posX, (int) posY, width, height);


        drawLives(g);
    }


    private void drawLives(Graphics g) {
        int heartSize = 8;
        int spacing = 2;

        int startX = (int) posX;
        int y = (int) posY - 10;

        g.setColor(Color.RED);

        for (int i = 0; i < lives; i++) {
            g.fillOval(startX + (i * (heartSize + spacing)), y, heartSize, heartSize);
        }
    }

    // ======================
    // MOVIMIENTO
    // ======================
    public void move(double dx, double dy) {
        if (!active) return;

        double finalDx = dx * speedMultiplier;
        double finalDy = dy * speedMultiplier;


        this.lastDx = finalDx;
        this.lastDy = finalDy;

        this.posX += finalDx;
        this.posY += finalDy;
    }


    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;

        lastDx = 0;
        lastDy = 0;
    }

    public void moveTo(double x, double y) {
        if (!active) return;
        this.posX = x;
        this.posY = y;
    }

    // ======================
    // VIDAS
    // ======================
    public void reduceLife() {
        if (lives <= 0) return;

        lives--;

        if (lives == 0) {
            active = false;
        }
    }

    public void setLives(int lives) {
        this.lives = lives;
        if (this.lives > 0) {
            this.active = true;
        }
    }

    public int getLives() {
        return lives;
    }

    // ======================
    // VELOCIDAD
    // ======================
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

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    // ======================
    // ESTADO
    // ======================
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

    // ======================
    // META
    // ======================
    public void setFinishLineReached(boolean reached) {
        this.finishLineReached = reached;
    }

    public boolean hasReachedFinishLine() {
        return finishLineReached;
    }
}