package edu.autonoma.turboclash.model;

/**
 * Item: Represents collectible objects like coins.
 * Pure model class: contains only data and logic, no rendering.
 */
public class Item extends GameObject {

    private final int scoreValue = 20;

    public Item(String id, double x, double y, int width, int height) {
        // We call the parent constructor (GameObject)
        super(id, x, y, width, height);
    }

    public int getScoreValue() {
        return scoreValue;
    }
}