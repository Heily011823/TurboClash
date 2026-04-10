package edu.autonoma.turboclash.domain.model;

/**
 * Representa la clase `Obstacle` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class Obstacle extends GameObject {


    private final ObstacleType type;

    private boolean processed = false;

    /**
     * Crea una nueva instancia de `Obstacle`.
     * @param id valor del parametro `id`
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param width valor del parametro `width`
     * @param height valor del parametro `height`
     * @param type valor del parametro `type`
     */
    public Obstacle(String id, double x, double y, int width, int height, ObstacleType type) {
        super(id, x, y, width, height);
        this.type = type;
    }

    /**
     * Obtiene el valor asociado a `getType`.
     * @return resultado de la operacion documentada
     */
    public ObstacleType getType() {
        return type;
    }

    /**
     * Indica la condicion evaluada por `isProcessed`.
     * @return resultado de la operacion documentada
     */
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Actualiza el valor asociado a `setProcessed`.
     * @param processed valor del parametro `processed`
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    /**
     * Obtiene el valor asociado a `getImage`.
     * @return resultado de la operacion documentada
     */
    public String getImage() {
        return type.getImage();
    }
}
