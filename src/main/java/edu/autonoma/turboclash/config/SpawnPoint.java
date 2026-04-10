package edu.autonoma.turboclash.config;

import edu.autonoma.turboclash.domain.model.ObstacleType;

/**
 * Representa la clase `SpawnPoint` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class SpawnPoint {

    private final int x;
    private final int y;
    private final ObstacleType type;

    /**
     * Crea una nueva instancia de `SpawnPoint`.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param type valor del parametro `type`
     */
    public SpawnPoint(int x, int y, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Obtiene el valor asociado a `getX`.
     * @return resultado de la operacion documentada
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene el valor asociado a `getY`.
     * @return resultado de la operacion documentada
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene el valor asociado a `getType`.
     * @return resultado de la operacion documentada
     */
    public ObstacleType getType() {
        return type;
    }
}
