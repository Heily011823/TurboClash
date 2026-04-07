package edu.autonoma.turboclash.model;

public class Car extends GameObject {

    // ======================
    // ESTADO DEL CARRO
    // ======================
    private double posX;
    private double posY;

    private double lastDx;
    private double lastDy;

    private boolean active;


    // VIDAS
    private int lives;


    // VELOCIDAD / DEBUFF

    private double speedMultiplier;
    private boolean debuffed;
    private long debuffEndTime;


    // META

    private boolean finishReached;


    // VISUAL

    private final String carImage;


    // CONSTRUCTOR

    public Car(String id, double x, double y, int width, int height, String carImage) {
        super(id, x, y, width, height);

        this.posX = x;
        this.posY = y;

        this.lastDx = 0;
        this.lastDy = 0;

        this.active = true;

        this.lives = 3;

        this.speedMultiplier = 1.0;
        this.debuffed = false;
        this.debuffEndTime = 0;

        this.finishReached = false;

        this.carImage = carImage;
    }

    // ======================
    // MOVIMIENTO
    // ======================

    public void move(double dx, double dy) {
        if (!active) {
            resetMovement();
            return;
        }

        this.lastDx = dx * speedMultiplier;
        this.lastDy = dy * speedMultiplier;

        this.posX += lastDx;
        this.posY += lastDy;

        updatePosition();
    }

    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;
        updatePosition();
    }

    public void stop() {
        resetMovement();
    }

    private void resetMovement() {
        this.lastDx = 0;
        this.lastDy = 0;
    }

    private void updatePosition() {
        super.setPosition(posX, posY);
    }


    // VELOCIDAD / DEBUFF


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

    public boolean isDebuffed() {
        return debuffed;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    // VIDAS


    public void reduceLife() {
        if (lives > 0) {
            lives--;
        }

        if (lives <= 0) {
            active = false;
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
    }

    // META


    public boolean isFinishReached() {
        return finishReached;
    }

    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }


    // ESTADO


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    // POSICIÓN


    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }


    // VISUAL


    public String getCarImage() {
        return carImage;
    }
}