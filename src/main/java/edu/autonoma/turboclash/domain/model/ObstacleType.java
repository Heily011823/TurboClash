package edu.autonoma.turboclash.domain.model;

public enum ObstacleType {
    OIL("Oil_Spill.png"),
    CONE("Cone.png"),
    BARRIER("Barrier.png");

    private final String image;

    ObstacleType(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
