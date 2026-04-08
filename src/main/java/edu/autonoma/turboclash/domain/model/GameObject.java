package edu.autonoma.turboclash.domain.model;

import java.awt.Rectangle;


/**
 * Representa la responsabilidad de {@code GameObject} dentro del dominio del juego.
 */
public abstract class GameObject {

    protected final String id;
    protected double posX;
    protected double posY;
    protected final int width;
    protected final int height;
    protected boolean visible = true;

    /**
     * Crea una nueva instancia de {@code GameObject}.
     *
     * @param id identificador asociado a la operacion
     * @param posX valor del parametro {@code posX}
     * @param posY valor del parametro {@code posY}
     * @param width valor del parametro {@code width}
     * @param height valor del parametro {@code height}
     */
    public GameObject(String id, double posX, double posY, int width, int height) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }


    /**
     * Obtiene el valor de {@code Bounds}.
     *
     * @return valor de {@code Bounds}
     */
    public Rectangle getBounds() {
        return new Rectangle(
                (int) posX,
                (int) posY,
                width,
                height
        );
    }


    /**
     * Ejecuta la operacion {@code collidesWith}.
     *
     * @param other valor del parametro {@code other}
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean collidesWith(GameObject other) {
        if (other == null || !this.visible || !other.visible) return false;
        return this.getBounds().intersects(other.getBounds());
    }


    /**
     * Actualiza el valor de {@code Position}.
     *
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     */
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public double getX() { return posX; }
    public double getY() { return posY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getId() { return id; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
