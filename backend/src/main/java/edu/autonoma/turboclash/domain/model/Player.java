package edu.autonoma.turboclash.domain.model;

/**
 * Representa la clase `Player` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class Player {

    private final String id;
    private final String name;
    private final Car car;
    private final Score score;
    private int networkPort;
    private long lastProcessedSequence;

    private boolean finishReached;
    private boolean eliminated;
    private int finishOrder;
    private int eliminationOrder;
    private boolean hasScored;

    /**
     * Crea una nueva instancia de `Player`.
     * @param id valor del parametro `id`
     * @param name valor del parametro `name`
     * @param car valor del parametro `car`
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
     * Ejecuta la operacion publica `move`.
     * @param dx valor del parametro `dx`
     * @param dy valor del parametro `dy`
     */
    public void move(double dx, double dy) {
        if (car != null) {
            car.move(dx, dy);
        }
    }

    /**
     * Sincroniza el estado asociado a sync from network.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param score valor del parametro `score`
     * @param lives valor del parametro `lives`
     */
    public void syncFromNetwork(double x, double y, int score, int lives) {
        syncFromNetwork(x, y, score, lives, finishReached, eliminated, finishOrder, eliminationOrder);
    }

    /**
     * Sincroniza el estado asociado a sync from network.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param score valor del parametro `score`
     * @param lives valor del parametro `lives`
     * @param finishReached valor del parametro `finishReached`
     * @param eliminated valor del parametro `eliminated`
     * @param finishOrder valor del parametro `finishOrder`
     * @param eliminationOrder valor del parametro `eliminationOrder`
     */
    public void syncFromNetwork(double x,
                                double y,
                                int score,
                                int lives,
                                boolean finishReached,
                                boolean eliminated,
                                int finishOrder,
                                int eliminationOrder) {
        if (car != null) {
            car.setPosition(x, y);
            car.setLives(lives);
            car.setFinishReached(finishReached);
        }

        this.score.setPoints(score);
        this.finishReached = finishReached;
        this.finishOrder = finishOrder;
        this.eliminationOrder = eliminationOrder;
        this.eliminated = eliminated || lives <= 0;

        if (score > 0) {
            this.hasScored = true;
        }

        if (lives <= 0) {
            this.eliminated = true;
        }
    }

    /**
     * Actualiza el estado relacionado con update score.
     * @param amount valor del parametro `amount`
     */
    public void updateScore(int amount) {
        score.update(amount);
        if (score.getPoints() > 0) {
            hasScored = true;
        }
    }

    /**
     * Obtiene el valor asociado a `getCurrentPoints`.
     * @return resultado de la operacion documentada
     */
    public int getCurrentPoints() {
        return score.getPoints();
    }

    /**
     * Actualiza el valor asociado a `setScore`.
     * @param points valor del parametro `points`
     */
    public void setScore(int points) {
        this.score.setPoints(points);
        if (points > 0) {
            hasScored = true;
        }
    }

    /**
     * Ejecuta la operacion publica `resetScore`.
     */
    public void resetScore() {
        this.score.setPoints(0);
    }

    /**
     * Indica la condicion evaluada por `hasScored`.
     * @return resultado de la operacion documentada
     */
    public boolean hasScored() {
        return hasScored;
    }

    /**
     * Actualiza el valor asociado a `setHasScored`.
     * @param value valor del parametro `value`
     */
    public void setHasScored(boolean value) {
        this.hasScored = value;
    }

    /**
     * Ejecuta la operacion publica `loseLife`.
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
     * Obtiene el valor asociado a `getLives`.
     * @return resultado de la operacion documentada
     */
    public int getLives() {
        return (car != null) ? car.getLives() : 0;
    }

    /**
     * Actualiza el valor asociado a `setLives`.
     * @param lives valor del parametro `lives`
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
     * Indica la condicion evaluada por `isAlive`.
     * @return resultado de la operacion documentada
     */
    public boolean isAlive() {
        return getLives() > 0 && !eliminated;
    }

    /**
     * Indica la condicion evaluada por `isFinishReached`.
     * @return resultado de la operacion documentada
     */
    public boolean isFinishReached() {
        return finishReached;
    }

    /**
     * Actualiza el valor asociado a `setFinishReached`.
     * @param finishReached valor del parametro `finishReached`
     */
    public void setFinishReached(boolean finishReached) {
        this.finishReached = finishReached;
    }

    /**
     * Indica la condicion evaluada por `isEliminated`.
     * @return resultado de la operacion documentada
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * Actualiza el valor asociado a `setEliminated`.
     * @param eliminated valor del parametro `eliminated`
     */
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    /**
     * Obtiene el valor asociado a `getFinishOrder`.
     * @return resultado de la operacion documentada
     */
    public int getFinishOrder() {
        return finishOrder;
    }

    /**
     * Actualiza el valor asociado a `setFinishOrder`.
     * @param finishOrder valor del parametro `finishOrder`
     */
    public void setFinishOrder(int finishOrder) {
        this.finishOrder = finishOrder;
    }

    /**
     * Obtiene el valor asociado a `getEliminationOrder`.
     * @return resultado de la operacion documentada
     */
    public int getEliminationOrder() {
        return eliminationOrder;
    }

    /**
     * Actualiza el valor asociado a `setEliminationOrder`.
     * @param eliminationOrder valor del parametro `eliminationOrder`
     */
    public void setEliminationOrder(int eliminationOrder) {
        this.eliminationOrder = eliminationOrder;
    }

    /**
     * Obtiene el valor asociado a `getCar`.
     * @return resultado de la operacion documentada
     */
    public Car getCar() {
        return car;
    }

    /**
     * Obtiene el valor asociado a `getNetworkPort`.
     * @return resultado de la operacion documentada
     */
    public int getNetworkPort() {
        return networkPort;
    }

    /**
     * Actualiza el valor asociado a `setNetworkPort`.
     * @param networkPort valor del parametro `networkPort`
     */
    public void setNetworkPort(int networkPort) {
        this.networkPort = networkPort;
    }

    /**
     * Obtiene el valor asociado a `getLastProcessedSequence`.
     * @return resultado de la operacion documentada
     */
    public long getLastProcessedSequence() {
        return lastProcessedSequence;
    }

    /**
     * Actualiza el valor asociado a `setLastProcessedSequence`.
     * @param lastProcessedSequence valor del parametro `lastProcessedSequence`
     */
    public void setLastProcessedSequence(long lastProcessedSequence) {
        this.lastProcessedSequence = lastProcessedSequence;
    }

    /**
     * Obtiene el valor asociado a `getId`.
     * @return resultado de la operacion documentada
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el valor asociado a `getName`.
     * @return resultado de la operacion documentada
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el valor asociado a `getScore`.
     * @return resultado de la operacion documentada
     */
    public Score getScore() {
        return score;
    }
}
