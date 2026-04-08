package edu.autonoma.turboclash.domain.model;

/**
 * Enumera los valores disponibles de {@code CarSkin} dentro del dominio del juego.
 */
public enum CarSkin {

    RED(0, "Car_Red.png"),
    BLUE(1, "Car_Blue.png"),
    YELLOW(2, "Car_Yellow.png"),
    BROWN(3, "Car_Brown.png");

    private final int id;
    private final String fileName;

    /**
     * Crea una nueva instancia de {@code CarSkin}.
     *
     * @param id identificador asociado a la operacion
     * @param fileName valor del parametro {@code fileName}
     */
    CarSkin(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    /**
     * Obtiene el valor de {@code Id}.
     *
     * @return valor de {@code Id}
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el valor de {@code FileName}.
     *
     * @return valor de {@code FileName}
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Ejecuta la operacion {@code fromIndex}.
     *
     * @param index valor del parametro {@code index}
     * @return resultado de la operacion {@code fromIndex}
     */
    public static CarSkin fromIndex(int index) {
        for (CarSkin skin : values()) {
            if (skin.id == index) {
                return skin;
            }
        }
        return BLUE;
    }
}
