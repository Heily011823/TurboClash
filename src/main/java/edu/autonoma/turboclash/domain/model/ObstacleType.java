package edu.autonoma.turboclash.domain.model;

/**
 * Enumera las opciones disponibles para `ObstacleType` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public enum ObstacleType {
    OIL("Oil_Spill.png"),
    CONE("Cone.png"),
    BARRIER("Barrier.png");

    private final String image;

    ObstacleType(String image) {
        this.image = image;
    }

    /**
     * Obtiene el valor asociado a `getImage`.
     * @return resultado de la operacion documentada
     */
    public String getImage() {
        return image;
    }
}
