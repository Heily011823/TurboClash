package edu.autonoma.turboclash.model;

public class Score {
    private int points = 0;

    public void update(int amount) {
        points += amount;
        if (points < 0) points = 0;
    }

    public int getPoints() {
        return points;
    }
}