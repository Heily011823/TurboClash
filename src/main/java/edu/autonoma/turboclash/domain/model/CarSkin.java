package edu.autonoma.turboclash.domain.model;

/**
 * Enumera las opciones disponibles para `CarSkin` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public enum CarSkin {

    RED(0, "Car_Red.png"),
    BLUE(1, "Car_Blue.png"),
    YELLOW(2, "Car_Yellow.png"),
    BROWN(3, "Car_Brown.png");

    private final int id;
    private final String fileName;

    CarSkin(int id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    /**
     * Obtiene el valor asociado a `getId`.
     * @return resultado de la operacion documentada
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el valor asociado a `getFileName`.
     * @return resultado de la operacion documentada
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Ejecuta la operacion publica `fromIndex`.
     * @param index valor del parametro `index`
     * @return resultado de la operacion documentada
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
