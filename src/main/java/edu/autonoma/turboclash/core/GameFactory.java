package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.model.*;

import java.util.ArrayList;

import java.util.List;



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


    public Player createPlayer(String playerId, String playerName, int puerto) {

        CarSkin skin = skinFactory.fromPort(puerto);

        Car car = new Car(
                playerId,
                0,
                0,
                80,
                40,
                skin.getFileName()
        );

        return new Player(playerId, playerName, car);
    }
}