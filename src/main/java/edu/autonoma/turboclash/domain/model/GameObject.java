package edu.autonoma.turboclash.domain.model;

import java.awt.Rectangle;


/**
 * Representa la clase `GameObject` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public abstract class GameObject {

    protected final String id;
    protected double posX;
    protected double posY;
    protected final int width;
    protected final int height;
    protected boolean visible = true;

    /**
     * Crea una nueva instancia de `GameObject`.
     * @param id valor del parametro `id`
     * @param posX valor del parametro `posX`
     * @param posY valor del parametro `posY`
     * @param width valor del parametro `width`
     * @param height valor del parametro `height`
     */
    public GameObject(String id, double posX, double posY, int width, int height) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }


    /**
     * Obtiene el valor asociado a `getBounds`.
     * @return resultado de la operacion documentada
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
     * Ejecuta la operacion publica `collidesWith`.
     * @param other valor del parametro `other`
     * @return resultado de la operacion documentada
     */
    public boolean collidesWith(GameObject other) {
        if (other == null || !this.visible || !other.visible) return false;
        return this.getBounds().intersects(other.getBounds());
    }


    /**
     * Actualiza el valor asociado a `setPosition`.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     */
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    /**
     * Obtiene el valor asociado a `getX`.
     * @return resultado de la operacion documentada
     */
    public double getX() { return posX; }
    /**
     * Obtiene el valor asociado a `getY`.
     * @return resultado de la operacion documentada
     */
    public double getY() { return posY; }
    /**
     * Obtiene el valor asociado a `getWidth`.
     * @return resultado de la operacion documentada
     */
    public int getWidth() { return width; }
    /**
     * Obtiene el valor asociado a `getHeight`.
     * @return resultado de la operacion documentada
     */
    public int getHeight() { return height; }
    /**
     * Obtiene el valor asociado a `getId`.
     * @return resultado de la operacion documentada
     */
    public String getId() { return id; }
    /**
     * Indica la condicion evaluada por `isVisible`.
     * @return resultado de la operacion documentada
     */
    public boolean isVisible() { return visible; }
    /**
     * Actualiza el valor asociado a `setVisible`.
     * @param visible valor del parametro `visible`
     */
    public void setVisible(boolean visible) { this.visible = visible; }
}
