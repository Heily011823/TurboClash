package edu.autonoma.turboclash.config;

import edu.autonoma.turboclash.domain.model.ObstacleType;

/**
 * Representa la responsabilidad de {@code SpawnPoint} en la configuracion del juego.
 */
public class SpawnPoint {

    private final int x;
    private final int y;
    private final ObstacleType type;

    /**
     * Crea una nueva instancia de {@code SpawnPoint}.
     *
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     * @param type valor del parametro {@code type}
     */
    public SpawnPoint(int x, int y, ObstacleType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Obtiene el valor de {@code X}.
     *
     * @return valor de {@code X}
     */
    public int getX() {
        return x;
    }

    /**
     * Obtiene el valor de {@code Y}.
     *
     * @return valor de {@code Y}
     */
    public int getY() {
        return y;
    }

    /**
     * Obtiene el valor de {@code Type}.
     *
     * @return valor de {@code Type}
     */
    public ObstacleType getType() {
        return type;
    }
}
