package edu.autonoma.turboclash.model;

public class CarSkinFactory {
    public static CarSkin fromPort(int puerto) {
        switch (puerto) {
            case 5000: return CarSkin.BLUE;
            case 5001: return CarSkin.RED;
            case 5002: return CarSkin.YELLOW;
            case 5003: return CarSkin.BROWN;
            default: return CarSkin.BLUE;
        }
    }
}
