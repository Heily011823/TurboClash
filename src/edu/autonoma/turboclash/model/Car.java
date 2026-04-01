package edu.autonoma.turboclash.model;

import java.awt.Graphics;

public class Car extends GameObject {

    private boolean active = true;

    public Car(String id, double x, double y, int w, int h) {
        super(id, x, y, w, h);
    }

    @Override
    public void draw(Graphics g) {
        if (!visible) return;
        g.fillRect((int) posX, (int) posY, width, height);
    }

    public void moveTo(double x, double y) {
        if (active) {
            posX = x;
            posY = y;
        }
    }
}