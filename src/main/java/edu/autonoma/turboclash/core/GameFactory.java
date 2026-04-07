package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.GameConstants;
import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.CarSkinFactory;
import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Player;

import java.util.ArrayList;

public class GameFactory {
    public Match createMatch(Player localPlayer) {
        return new Match(localPlayer, new ArrayList<>(), GameConstants.DEFAULT_TARGET_SCORE);
    }

    public Player createPlayer(String id, String name, int puerto) {
        Car car = new Car(id, 50.0, 300.0, CarSkinFactory.fromPort(puerto));
        Player player = new Player(id, name, car);
        player.setLives(GameConstants.INITIAL_LIVES);
        return player;
    }
}
