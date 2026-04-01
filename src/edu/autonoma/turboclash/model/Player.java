package edu.autonoma.turboclash.model;

public class Player {

    private final String id;
    private final String name;
    private final Car car;
    private final Score score = new Score();

    public Player(String id, String name, Car car) {
        this.id = id;
        this.name = name;
        this.car = car;
    }

    public void moveCar(double x, double y) {
        car.moveTo(x, y);
    }

    public void updateScore(int amount) {
        score.update(amount);
    }

    public int getCurrentPoints() {
        return score.getPoints();
    }

    public Car getCar() { return car; }
    public String getId() { return id; }
    public String getName() { return name; }
}