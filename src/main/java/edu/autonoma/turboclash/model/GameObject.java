package edu.autonoma.turboclash.model;

import java.awt.Graphics;

public abstract class GameObject {

    protected final String id;
    protected double posX;
    protected double posY;
    protected final int width;
    protected final int height;
    protected boolean visible = true;

    public GameObject(String id, double posX, double posY, int width, int height) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    //  MÉTODO ABSTRACTO (OBLIGATORIO EN HIJOS)
    public abstract void draw(Graphics g);

    //  COLISIÓN (AABB)
    public boolean collidesWith(GameObject other) {
        if (other == null || !this.visible || !other.visible) return false;

        return this.posX < other.posX + other.width &&
                this.posX + this.width > other.posX &&
                this.posY < other.posY + other.height &&
                this.posY + this.height > other.posY;
    }

    // GETTERS BÁSICOS
    public String getId() { return id; }
    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public boolean isVisible() { return visible; }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}