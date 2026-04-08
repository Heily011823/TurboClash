package edu.autonoma.turboclash.domain.model;


public class Item extends GameObject {

    private final int scoreValue = 20;

    public Item(String id, double x, double y, int width, int height) {
        super(id, x, y, width, height);
    }

    public int getScoreValue() {
        return scoreValue;
    }
}