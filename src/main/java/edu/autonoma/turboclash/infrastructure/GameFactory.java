package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.*;

import java.util.ArrayList;


/**
 * Crea y configura instancias relacionadas con {@code GameFactory} en la infraestructura del sistema.
 */
public class GameFactory {

    private final GameConfig config;
    private final CarSkinFactory skinFactory;

    /**
     * Crea una nueva instancia de {@code GameFactory}.
     *
     * @param config valor del parametro {@code config}
     */
    public GameFactory(GameConfig config) {
        this.config = config;
        this.skinFactory = new CarSkinFactory(config);
    }

    /**
     * Crea {@code Match}.
     *
     * @param localPlayer valor del parametro {@code localPlayer}
     * @return instancia creada para {@code Match}
     */
    public Match createMatch(Player localPlayer) {
        return new Match(localPlayer, new ArrayList<>(), config.getTargetScore());
    }


    /**
     * Crea {@code Player}.
     *
     * @param playerId valor del parametro {@code playerId}
     * @param playerName valor del parametro {@code playerName}
     * @param puerto valor del parametro {@code puerto}
     * @return instancia creada para {@code Player}
     */
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
