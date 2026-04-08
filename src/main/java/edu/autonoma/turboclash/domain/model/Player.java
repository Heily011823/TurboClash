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

    // PUNTAJE
    /**
     * Actualiza {@code Score}.
     *
     * @param amount valor del parametro {@code amount}
     */
    public void updateScore(int amount) {
        score.update(amount);

        if (score.getPoints() > 0) {
            hasScored = true;
        }
    }

    /**
     * Obtiene el valor de {@code CurrentPoints}.
     *
     * @return valor de {@code CurrentPoints}
     */
    public int getCurrentPoints() {
        return score.getPoints();
    }

    /**
     * Actualiza el valor de {@code Score}.
     *
     * @param points valor del parametro {@code points}
     */
    public void setScore(int points) {
        this.score.setPoints(points);

        if (points > 0) {
            hasScored = true;
        }
    }

    /**
     * Reinicia {@code Score}.
     */
    public void resetScore() {
        this.score.setPoints(0);
    }

    /**
     * Indica si {@code Scored}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean hasScored() {
        return hasScored;
    }

    /**
     * Actualiza el valor de {@code HasScored}.
     *
     * @param value valor del parametro {@code value}
     */
    public void setHasScored(boolean value) {
        this.hasScored = value;
    }


    /**
     * Ejecuta la operacion {@code loseLife}.
     */
    public void loseLife() {
        if (car != null) {
            car.reduceLife();

            if (car.getLives() <= 0) {
                eliminated = true;
            }
        }
    }

    /**
     * Obtiene el valor de {@code Lives}.
     *
     * @return valor de {@code Lives}
     */
    public int getLives() {
        return (car != null) ? car.getLives() : 0;
    }

    /**
     * Actualiza el valor de {@code Lives}.
     *
     * @param lives valor del parametro {@code lives}
     */
    public void setLives(int lives) {
        if (car != null) {
            car.setLives(lives);

            if (car.getLives() <= 0) {
                eliminated = true;
            }
        }
    }

    /**
     * Indica si {@code Alive}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isAlive() {
        return getLives() > 0;
    }


    /**
     * Indica si {@code FinishReached}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isFinishReached() {
        return finishReached;
    }

    /**
     * Actualiza el valor de {@code FinishReached}.
     *
     * @param finishReached valor del parametro {@code finishReached}
     */
    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }

    /**
     * Indica si {@code Eliminated}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Actualiza el valor de {@code Eliminated}.
     *
     * @param eliminated valor del parametro {@code eliminated}
     */
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    /**
     * Obtiene el valor de {@code FinishOrder}.
     *
     * @return valor de {@code FinishOrder}
     */
    public int getFinishOrder() {
        return finishOrder;
    }

    /**
     * Actualiza el valor de {@code FinishOrder}.
     *
     * @param finishOrder valor del parametro {@code finishOrder}
     */
    public void setFinishOrder(int finishOrder) {
        this.finishOrder = finishOrder;
    }

    /**
     * Obtiene el valor de {@code EliminationOrder}.
     *
     * @return valor de {@code EliminationOrder}
     */
    public int getEliminationOrder() {
        return eliminationOrder;
    }

    /**
     * Actualiza el valor de {@code EliminationOrder}.
     *
     * @param eliminationOrder valor del parametro {@code eliminationOrder}
     */
    public void setEliminationOrder(int eliminationOrder) {
        this.eliminationOrder = eliminationOrder;
    }


    /**
     * Obtiene el valor de {@code Car}.
     *
     * @return valor de {@code Car}
     */
    public Car getCar() {
        return car;
    }

    /**
     * Obtiene el valor de {@code Id}.
     *
     * @return valor de {@code Id}
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el valor de {@code Name}.
     *
     * @return valor de {@code Name}
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el valor de {@code Score}.
     *
     * @return valor de {@code Score}
     */
    public Score getScore() {
        return score;
    }
}
