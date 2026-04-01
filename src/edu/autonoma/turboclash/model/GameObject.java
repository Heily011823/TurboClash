package edu.autonoma.turboclash.model;

import java.awt.Graphics;

public abstract class GameObject {
    protected final String id;
    protected double posX, posY;
    protected final int width, height;
    protected boolean visible = true;

    public GameObject(String id, double x, double y, int w, int h) {
        this.id = id;
        this.posX = x;
        this.posY = y;
        this.width = w;
        this.height = h;
    }

    public abstract void draw(Graphics g);

    public boolean collidesWith(GameObject other) {
        if (other == null || !visible || !other.visible) return false;

        return posX < other.posX + other.width &&
                posX + width > other.posX &&
                posY < other.posY + other.height &&
                posY + height > other.posY;
    }

    public boolean isVisible() { return visible; }
}