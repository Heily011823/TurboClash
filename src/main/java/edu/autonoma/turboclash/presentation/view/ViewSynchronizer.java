package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.List;

/**
 * Representa la responsabilidad de {@code ViewSynchronizer} en la capa de presentacion.
 */
public class ViewSynchronizer {

    /**
     * Sincroniza la operacion principal del metodo.
     *
     * @param window valor del parametro {@code window}
     * @param match valor del parametro {@code match}
     * @param obstacles valor del parametro {@code obstacles}
     * @param items valor del parametro {@code items}
     */
    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null) return;

        window.updateScore(localPlayer.getCurrentPoints());


        window.updateHealth(localPlayer.getCar(), localPlayer.getLives());

        window.updateCars(match.getPlayers());
        window.updateObstacles(obstacles);
        window.updateItems(items);

        if (window.getPanel() != null) {
            window.getPanel().repaint();
        }
    }
}
