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


    public void move(double dx, double dy) {
        if (car != null) {
            car.move(dx, dy);
        }
    }


    public void syncFromNetwork(double x, double y, int score) {
        if (car != null) {
            car.setPosition(x, y);
        }
        this.setScore(score);
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


    public void loseLife() {
        if (car != null) {
            car.reduceLife();
        }
    }

    public int getLives() {
        return (car != null) ? car.getLives() : 0;
    }

    public void setLives(int lives) {
        if (car != null) {
            car.setLives(lives);
        }
    }


    public Car getCar() { return car; }
    public String getId() { return id; }
    public String getName() { return name; }
    public Score getScore() { return score; }
}