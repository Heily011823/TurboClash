package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.List;

/**
 * Representa la clase `ViewSynchronizer` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class ViewSynchronizer {

    /**
     * Ejecuta la operacion publica `sync`.
     * @param window valor del parametro `window`
     * @param match valor del parametro `match`
     * @param obstacles valor del parametro `obstacles`
     * @param items valor del parametro `items`
     */
    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null) return;

        window.updateScore(localPlayer.getCurrentPoints());
        window.updatePlayerStatus(match.getPlayers());
        window.updateCars(match.getPlayers());
        window.updateObstacles(obstacles);
        window.updateItems(items);
    }
}
