package edu.autonoma.turboclash.model;

public class Player {

    private final String id;
    private final String name;
    private final Car car;
    private final Score score;

    public Player(String id, String name, Car car) {
        this.id = id;
        this.name = name;
        this.car = car;
        this.score = new Score();
    }

    public void move(double x, double y) {
        car.moveTo(x, y);
    }

    public void updateScore(int amount) {
        score.update(amount);
    }

    public int getCurrentPoints() {
        return score.getPoints();
    }


    public void setScore(int points) {
        this.score.setPoints(points);
    }

    // getters
    public Car getCar() { return car; }
    public String getId() { return id; }
    public String getName() { return name; }

    public Score getScore() {
        return score;
    }
}