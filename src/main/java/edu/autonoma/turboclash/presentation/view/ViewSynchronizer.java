package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.List;

public class ViewSynchronizer {

    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null) return;

        window.updateScore(localPlayer.getCurrentPoints());
        window.prepareRaceStart(match.getPlayers());
        window.updateCars(match.getPlayers());
        window.updateObstacles(obstacles);
        window.updateItems(items);

        if (window.getPanel() != null) {
            window.getPanel().repaint();
        }
    }
}