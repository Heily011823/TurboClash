package edu.autonoma.turboclash.infrastructure;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.*;
import edu.autonoma.turboclash.presentation.view.GameViewport;

import java.util.ArrayList;


/**
 * Representa la clase `GameFactory` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameFactory {

    private final GameConfig config;
    private final CarSkinFactory skinFactory;

    /**
     * Crea una nueva instancia de `GameFactory`.
     * @param config valor del parametro `config`
     */
    public GameFactory(GameConfig config) {
        this.config = config;
        this.skinFactory = new CarSkinFactory(config);
    }

    /**
     * Crea el recurso necesario para create match.
     * @param localPlayer valor del parametro `localPlayer`
     * @return resultado de la operacion documentada
     */
    public Match createMatch(Player localPlayer) {
        return new Match(localPlayer, new ArrayList<>(), config.getTargetScore());
    }


    /**
     * Crea el recurso necesario para create player.
     * @param playerId valor del parametro `playerId`
     * @param playerName valor del parametro `playerName`
     * @param puerto valor del parametro `puerto`
     * @return resultado de la operacion documentada
     */
    public Player createPlayer(String playerId, String playerName, int puerto) {

        CarSkin skin = skinFactory.fromPort(puerto);

        Car car = new Car(
                playerId,
                GameViewport.CAR_START_X,
                resolveLaneY(puerto),
                80,
                40,
                skin.getFileName()
        );

        Player player = new Player(playerId, playerName, car);
        player.setNetworkPort(puerto);
        return player;
    }

    private int resolveLaneY(int puerto) {
        return switch (puerto) {
            case 5001 -> GameViewport.laneY(0);
            case 5002 -> GameViewport.laneY(1);
            case 5003 -> GameViewport.laneY(2);
            case 5004 -> GameViewport.laneY(3);
            default -> GameViewport.laneY(0);
        };
    }
}
