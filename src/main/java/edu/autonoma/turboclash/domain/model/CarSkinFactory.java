package edu.autonoma.turboclash.domain.model;

import edu.autonoma.turboclash.config.GameConfig;

public class CarSkinFactory {

    private final GameConfig config;

    public CarSkinFactory(GameConfig config) {
        this.config = config;
    }

    public CarSkin fromPort(int puerto) {

        int index = puerto - config.getMinPort();

        return CarSkin.fromIndex(index);
    }
}