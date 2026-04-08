package edu.autonoma.turboclash.domain.model;

/**
 * Representa la responsabilidad de {@code Car} dentro del dominio del juego.
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
     * Crea una nueva instancia de {@code Car}.
     *
     * @param id identificador asociado a la operacion
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     * @param width valor del parametro {@code width}
     * @param height valor del parametro {@code height}
     * @param carImage valor del parametro {@code carImage}
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
     * Desplaza la operacion principal del metodo.
     *
     * @param dx desplazamiento horizontal aplicado en la operacion
     * @param dy desplazamiento vertical aplicado en la operacion
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
     * Detiene la operacion principal del metodo.
     */
    public void stop() {
        this.lastDx = 0;
        this.lastDy = 0;
    }

    /**
     * Ejecuta la operacion {@code undoLastMove}.
     */
    public void undoLastMove() {
        this.posX -= lastDx;
        this.posY -= lastDy;
        updatePosition();
    }

    /**
     * Actualiza {@code Position}.
     */
    private void updatePosition() {
        super.setPosition(posX, posY);
    }

    /**
     * Mantiene sincronizadas las coordenadas internas del carro con la posicion del GameObject.
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     */
    @Override
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
        super.setPosition(x, y);
    }

    /**
     * Ejecuta la operacion {@code applyDebuff}.
     *
     * @param factor valor del parametro {@code factor}
     * @param duration valor del parametro {@code duration}
     */
    public void applyDebuff(double factor, long duration) {
        this.speedMultiplier = factor;
        this.debuffed = true;
        this.debuffEndTime = System.currentTimeMillis() + duration;
    }

    /**
     * Actualiza {@code Debuff}.
     */
    public void updateDebuff() {
        if (debuffed && System.currentTimeMillis() > debuffEndTime) {
            this.speedMultiplier = 1.0;
            this.debuffed = false;
        }
    }

    /**
     * Ejecuta la operacion {@code reduceLife}.
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
     * Actualiza el valor de {@code Lives}.
     *
     * @param lives valor del parametro {@code lives}
     */
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
        this.active = this.lives > 0;
    }

    /**
     * Obtiene el valor de {@code Lives}.
     *
     * @return valor de {@code Lives}
     */
    public int getLives() {
        return lives;
    }

    /**
     * Indica si {@code FinishReached}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isFinishReached() {
        return finishReached;
    }

    /**
     * Actualiza el valor de {@code FinishReached}.
     *
     * @param finishReached valor del parametro {@code finishReached}
     */
    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }

    /**
     * Indica si {@code Active}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Obtiene el valor de {@code X}.
     *
     * @return valor de {@code X}
     */
    public double getX() {
        return posX;
    }

    /**
     * Obtiene el valor de {@code Y}.
     *
     * @return valor de {@code Y}
     */
    public double getY() {
        return posY;
    }

    /**
     * Obtiene el valor de {@code CarImage}.
     *
     * @return valor de {@code CarImage}
     */
    public String getCarImage() {
        return carImage;
    }

    /**
     * Indica si {@code Debuffed}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isDebuffed() {
        return debuffed;
    }

    /**
     * Obtiene el valor de {@code SpeedMultiplier}.
     *
     * @return valor de {@code SpeedMultiplier}
     */
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}