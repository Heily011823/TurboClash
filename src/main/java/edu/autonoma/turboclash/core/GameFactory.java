package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.model.*;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
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

        Car car = new Car(playerName, 0, 0, CarSkin.BLUE); // luego puedes mejorar skin
        car.setPlayerName(playerName);

        Player player = new Player(playerId, playerName, car);

        return player;
    }
}
