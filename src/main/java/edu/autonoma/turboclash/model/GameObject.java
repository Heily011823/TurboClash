package edu.autonoma.turboclash.model;

import java.awt.Graphics;
import java.awt.Rectangle;

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

    public abstract void draw(Graphics g);


    public Rectangle getBounds() {
        return new Rectangle(
                (int) posX,
                (int) posY,
                width,
                height
        );
    }


    public boolean collidesWith(GameObject other) {
        if (other == null || !this.visible || !other.visible) return false;

        return this.getBounds().intersects(other.getBounds());
    }


    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public double getX() { return posX; }
    public double getY() { return posY; }


    public String getId() { return id; }
    public boolean isVisible() { return visible; }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}