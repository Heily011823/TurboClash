package edu.autonoma.turboclash.domain.model;

/**
 * Representa la clase `Car` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
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

    /**
     * Crea una nueva instancia de `Car`.
     * @param id valor del parametro `id`
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param width valor del parametro `width`
     * @param height valor del parametro `height`
     * @param carImage valor del parametro `carImage`
     */
    public Car(String id, double x, double y, int width, int height, String carImage) {
        super(id, x, y, width, height);
        this.posX = x;
        this.posY = y;
        this.active = true;
        this.lives = 3;
        this.finishReached = false;
        this.carImage = carImage;
    }

    /**
     * Ejecuta la operacion publica `move`.
     * @param dx valor del parametro `dx`
     * @param dy valor del parametro `dy`
     */
    public void move(double dx, double dy) {
        if (!active) return;

        double baseSpeed = 12.0;
        this.lastDx = dx * baseSpeed * speedMultiplier;
        this.lastDy = dy * 6.0 * speedMultiplier;
        this.posX += lastDx;
        this.posY += lastDy;
        updatePosition();
    }

    /**
     * Ejecuta la operacion publica `stop`.
     */
    public void stop() {
        this.lastDx = 0;
        this.lastDy = 0;
    }

    /**
     * Ejecuta la operacion publica `undoLastMove`.
     */
    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;
        updatePosition();
    }

    private void updatePosition() {
        super.setPosition(posX, posY);
    }

    @Override
    /**
     * Actualiza el valor asociado a `setPosition`.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     */
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
        super.setPosition(x, y);
    }

    /**
     * Aplica la logica correspondiente a apply debuff.
     * @param factor valor del parametro `factor`
     * @param duration valor del parametro `duration`
     */
    public void applyDebuff(double factor, long duration) {
        this.speedMultiplier = factor;
        this.debuffed = true;
        this.debuffEndTime = System.currentTimeMillis() + duration;
    }

    /**
     * Actualiza el estado relacionado con update debuff.
     */
    public void updateDebuff() {
        if (debuffed && System.currentTimeMillis() > debuffEndTime) {
            this.speedMultiplier = 1.0;
            this.debuffed = false;
        }
    }

    /**
     * Ejecuta la operacion publica `reduceLife`.
     */
    public void reduceLife() {
        if (lives > 0) {
            lives--;
        }

        if (lives <= 0) {
            lives = 0;
            active = false;
        }
    }

    /**
     * Actualiza el valor asociado a `setLives`.
     * @param lives valor del parametro `lives`
     */
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
        this.active = this.lives > 0;
    }

    /**
     * Obtiene el valor asociado a `getLives`.
     * @return resultado de la operacion documentada
     */
    public int getLives() {
        return lives;
    }

    /**
     * Indica la condicion evaluada por `isFinishReached`.
     * @return resultado de la operacion documentada
     */
    public boolean isFinishReached() {
        return finishReached;
    }

    /**
     * Actualiza el valor asociado a `setFinishReached`.
     * @param finishReached valor del parametro `finishReached`
     */
    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }

    /**
     * Indica la condicion evaluada por `isActive`.
     * @return resultado de la operacion documentada
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Obtiene el valor asociado a `getX`.
     * @return resultado de la operacion documentada
     */
    public double getX() {
        return posX;
    }

    /**
     * Obtiene el valor asociado a `getY`.
     * @return resultado de la operacion documentada
     */
    public double getY() {
        return posY;
    }

    /**
     * Obtiene el valor asociado a `getCarImage`.
     * @return resultado de la operacion documentada
     */
    public String getCarImage() {
        return carImage;
    }

    /**
     * Indica la condicion evaluada por `isDebuffed`.
     * @return resultado de la operacion documentada
     */
    public boolean isDebuffed() {
        return debuffed;
    }

    /**
     * Obtiene el valor asociado a `getSpeedMultiplier`.
     * @return resultado de la operacion documentada
     */
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
