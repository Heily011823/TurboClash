package edu.autonoma.turboclash.domain.model;

/**
 * Representa la clase `Score` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class Score {
    private int points = 0;

    /**
     * Ejecuta la operacion publica `update`.
     * @param amount valor del parametro `amount`
     */
    public void update(int amount) {
        points += amount;
        if (points < 0) points = 0;
    }

    /**
     * Obtiene el valor asociado a `getPoints`.
     * @return resultado de la operacion documentada
     */
    public int getPoints() {
        return points;
    }
    /**
     * Actualiza el valor asociado a `setPoints`.
     * @param points valor del parametro `points`
     */
    public void setPoints(int points) {
        this.points = points;
    }
}
