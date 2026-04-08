package edu.autonoma.turboclash.domain.model;

public class Car extends GameObject {
    private double posX, posY;
    private double lastDx, lastDy;
    private boolean active;
    private int lives;
    private double speedMultiplier = 1.0;
    private boolean debuffed;
    private long debuffEndTime;
    private boolean finishReached;
    private final String carImage;

    public Car(String id, double x, double y, int width, int height, String carImage) {
        super(id, x, y, width, height);
        this.posX = x;
        this.posY = y;
        this.active = true;
        this.lives = 3;
        this.carImage = carImage;
    }

    public void move(double dx, double dy) {
        if (!active) return;
        double baseSpeed = 12.0;
        this.lastDx = dx * baseSpeed * speedMultiplier;
        this.lastDy = dy * 6.0 * speedMultiplier;
        this.posX += lastDx;
        this.posY += lastDy;
        updatePosition();
    }

    public void stop() {
        this.lastDx = 0;
        this.lastDy = 0;
    }

    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;
        updatePosition();
    }

    private void updatePosition() {
        super.setPosition(posX, posY);
    }

    public void applyDebuff(double factor, long duration) {
        this.speedMultiplier = factor;
        this.debuffed = true;
        this.debuffEndTime = System.currentTimeMillis() + duration;
    }

    public void updateDebuff() {
        if (debuffed && System.currentTimeMillis() > debuffEndTime) {
            this.speedMultiplier = 1.0;
            this.debuffed = false;
        }
    }

    public void reduceLife() {
        if (lives > 0) lives--;
        if (lives <= 0) active = false;
    }
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
    }
    public int getLives() { return lives; }
    public boolean isFinishReached() { return finishReached; }
    public void setFinishReached(boolean finishReached) { this.finishReached = finishReached; }
    public boolean isActive() { return active; }
    public double getX() { return posX; }
    public double getY() { return posY; }
    public String getCarImage() { return carImage; }
}