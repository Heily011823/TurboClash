package edu.autonoma.turboclash.model;

import java.awt.Graphics;

public class Obstacle extends GameObject {

    private final int penalty;

    public Obstacle(String id, double x, double y, int w, int h, int penalty) {
        super(id, x, y, w, h);
        this.penalty = Math.abs(penalty);
    }

    public void affectPlayer(Player player) {
        if (player != null && visible) {
            player.updateScore(-penalty);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!visible) return;
        g.fillRect((int) posX, (int) posY, width, height);
    }
}