package edu.autonoma.turboclash.domain.model;

/**
 * Representa la responsabilidad de {@code Score} dentro del dominio del juego.
 */
public class Score {
    private int points = 0;

    /**
     * Actualiza la operacion principal del metodo.
     *
     * @param amount valor del parametro {@code amount}
     */
    public void update(int amount) {
        points += amount;
        if (points < 0) points = 0;
    }

    /**
     * Obtiene el valor de {@code Points}.
     *
     * @return valor de {@code Points}
     */
    public int getPoints() {
        return points;
    }
    /**
     * Actualiza el valor de {@code Points}.
     *
     * @param points valor del parametro {@code points}
     */
    public void setPoints(int points) {
        this.points = points;
    }
}
