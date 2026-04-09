package edu.autonoma.turboclash.domain.model;

public class Player {

    private final String id;
    private final String name;
    private final Car car;
    private final Score score;

    private boolean finishReached;
    private boolean eliminated;
    private int finishOrder;
    private int eliminationOrder;
    private boolean hasScored;

    public Player(String id, String name, Car car) {
        this.id = id;
        this.name = name;
        this.car = car;
        this.score = new Score();
        this.hasScored = false;
        this.finishReached = false;
        this.eliminated = false;
        this.finishOrder = Integer.MAX_VALUE;
        this.eliminationOrder = Integer.MAX_VALUE;
    }

    public void move(double dx, double dy) {
        if (car != null) {
            car.move(dx, dy);
        }
    }

    public void syncFromNetwork(double x, double y, int score, int lives) {
        if (car != null) {
            car.setPosition(x, y);
            car.setLives(lives);
        }

        this.score.setPoints(score);

        if (score > 0) {
            this.hasScored = true;
        }

        if (lives <= 0) {
            this.eliminated = true;
        }
    }

    public void updateScore(int amount) {
        score.update(amount);
        if (score.getPoints() > 0) {
            hasScored = true;
        }
    }

    public int getCurrentPoints() {
        return score.getPoints();
    }

    public void setScore(int points) {
        this.score.setPoints(points);
        if (points > 0) {
            hasScored = true;
        }
    }

    public void resetScore() {
        this.score.setPoints(0);
    }

    public boolean hasScored() {
        return hasScored;
    }

    public void setHasScored(boolean value) {
        this.hasScored = value;
    }

    public void loseLife() {
        if (car != null) {
            car.reduceLife();
            if (car.getLives() <= 0) {
                eliminated = true;
            }
        }
    }

    public int getLives() {
        return (car != null) ? car.getLives() : 0;
    }

    public void setLives(int lives) {
        if (car != null) {
            car.setLives(lives);
            if (car.getLives() <= 0) {
                eliminated = true;
            }
        }
    }

    public boolean isAlive() {
        return getLives() > 0;
    }

    public boolean isFinishReached() {
        return finishReached;
    }

    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public int getFinishOrder() {
        return finishOrder;
    }

    public void setFinishOrder(int finishOrder) {
        this.finishOrder = finishOrder;
    }

    public int getEliminationOrder() {
        return eliminationOrder;
    }

    public void setEliminationOrder(int eliminationOrder) {
        this.eliminationOrder = eliminationOrder;
    }

    public Car getCar() {
        return car;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Score getScore() {
        return score;
    }
}