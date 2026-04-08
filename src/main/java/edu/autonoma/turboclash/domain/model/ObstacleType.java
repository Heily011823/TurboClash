package edu.autonoma.turboclash.domain.model;

/**
 * Enumera los valores disponibles de {@code ObstacleType} dentro del dominio del juego.
 */
public enum ObstacleType {
    OIL("Oil_Spill.png"),
    CONE("Cone.png"),
    BARRIER("Barrier.png");

    private final String image;

    /**
     * Crea una nueva instancia de {@code ObstacleType}.
     *
     * @param image valor del parametro {@code image}
     */
    ObstacleType(String image) {
        this.image = image;
    }

    /**
     * Obtiene el valor de {@code Image}.
     *
     * @return valor de {@code Image}
     */
    public String getImage() {
        return image;
    }
}
