package edu.autonoma.turboclash.domain.model;

/**
 * Enumera las opciones disponibles para `ObstacleType` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
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
