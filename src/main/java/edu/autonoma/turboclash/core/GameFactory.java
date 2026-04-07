package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.model.*;

import java.util.ArrayList;

public class GameFactory {

    private final GameConfig config;
    private final CarSkinFactory skinFactory;

    public GameFactory(GameConfig config) {
        this.config = config;
        this.skinFactory = new CarSkinFactory(config);
    }

    public Match createMatch(Player localPlayer) {
        return new Match(localPlayer, new ArrayList<>(), config.getTargetScore());
    }

    public Player createPlayer(String id, String name, int puerto) {

        Car car = new Car(
                id,
                config.getCarStartX(),
                config.getCarStartY(),
                skinFactory.fromPort(puerto)
        );

        Player player = new Player(id, name, car);
        player.setLives(config.getInitialLives());

        return player;
    }
}