package edu.autonoma.turboclash.model;

import edu.autonoma.turboclash.logic.GameConstants;
import java.awt.Graphics;
import java.awt.Color;

public class Obstacle extends GameObject {

    private int penalty = GameConstants.OBSTACLE_PENALTY;

    public Obstacle(String id, double x, double y, int w, int h) {
        super(id, x, y, w, h);
    }


    public void affectPlayer(Player player) {

        if (player != null && isVisible()) {
            player.updateScore(-penalty);
        }
    }

    @Override
    public void draw(Graphics g) {
        if (!isVisible()) return; // Usamos el getter heredado
        g.setColor(Color.RED);
        g.fillRect((int) posX, (int) posY, width, height);
    }
}