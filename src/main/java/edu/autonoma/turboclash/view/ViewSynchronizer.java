package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.*;
import java.util.List;


public class ViewSynchronizer {

    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null) return;


        window.updateScore(localPlayer.getCurrentPoints());


        window.updateHealth(localPlayer.getLives(), localPlayer.getCar());


        window.updateCars(match.getPlayers());
        window.updateObstacles(obstacles);
        window.updateItems(items);


        if (window.getPanel() != null) {
            window.getPanel().repaint();
        }
    }
}