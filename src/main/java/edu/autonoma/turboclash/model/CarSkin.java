package edu.autonoma.turboclash.model;

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

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public static CarSkin fromIndex(int index) {
        for (CarSkin skin : values()) {
            if (skin.id == index) {
                return skin;
            }
        }
        return BLUE;
    }
}