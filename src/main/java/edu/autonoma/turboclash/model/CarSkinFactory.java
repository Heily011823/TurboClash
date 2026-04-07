package edu.autonoma.turboclash.model;

import edu.autonoma.turboclash.config.GameConfig;

public class CarSkinFactory {

    public static CarSkin fromPort(int puerto) {

        int index = puerto - GameConfig.MIN_PORT;

        return CarSkin.fromIndex(index);
    }
}