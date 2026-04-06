package edu.autonoma.turboclash.model;

public enum CarSkin {
    RED("Car_Red.png"),
    BLUE("Car_Blue.png"),
    YELLOW("Car_Yellow.png"),
    BROWN("Car_Brown.png");

    private final String fileName;

    CarSkin(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
