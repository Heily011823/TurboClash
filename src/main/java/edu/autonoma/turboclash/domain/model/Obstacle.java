package edu.autonoma.turboclash.domain.model;

/**
 * Representa la responsabilidad de {@code Obstacle} dentro del dominio del juego.
 */
public class Obstacle extends GameObject {


    private final ObstacleType type;

    private boolean processed = false;

    /**
     * Crea una nueva instancia de {@code Obstacle}.
     *
     * @param id identificador asociado a la operacion
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     * @param width valor del parametro {@code width}
     * @param height valor del parametro {@code height}
     * @param type valor del parametro {@code type}
     */
    public Obstacle(String id, double x, double y, int width, int height, ObstacleType type) {
        super(id, x, y, width, height);
        this.type = type;
    }

    /**
     * Obtiene el valor de {@code Type}.
     *
     * @return valor de {@code Type}
     */
    public ObstacleType getType() {
        return type;
    }

    /**
     * Indica si {@code Processed}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Actualiza el valor de {@code Processed}.
     *
     * @param processed valor del parametro {@code processed}
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * Obtiene el valor de {@code Image}.
     *
     * @return valor de {@code Image}
     */
    public String getImage() {
        return type.getImage();
    }
}
