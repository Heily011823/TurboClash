package edu.autonoma.turboclash.domain.model;

/**
 * Representa la responsabilidad de {@code Player} dentro del dominio del juego.
 */
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

    /**
     * Crea una nueva instancia de {@code Player}.
     *
     * @param id identificador asociado a la operacion
     * @param name valor del parametro {@code name}
     * @param car valor del parametro {@code car}
     */
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

    /**
     * Desplaza la operacion principal del metodo.
     *
     * @param dx desplazamiento horizontal aplicado en la operacion
     * @param dy desplazamiento vertical aplicado en la operacion
     */
    public void move(double dx, double dy) {
        if (car != null) {
            car.move(dx, dy);
        }
    }

    /**
     * Sincroniza {@code FromNetwork}.
     *
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     * @param score valor del parametro {@code score}
     */
    public void syncFromNetwork(double x, double y, int score) {
        if (car != null) {
            car.setPosition(x, y);
        }
        this.score.setPoints(score);

        if (score > 0) {
            this.hasScored = true;
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