package edu.autonoma.turboclash.model;

import java.awt.Graphics;

public class Item extends GameObject {

    private final int value;

    public Item(String id, double x, double y, int w, int h, int value) {
        super(id, x, y, w, h);
        this.value = Math.max(0, value);
    }

    public void applyEffect(Player player) {
        if (player != null && visible) {
            player.updateScore(value);
            visible = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!visible) return;
        g.fillOval((int) posX, (int) posY, width, height);
    }
}