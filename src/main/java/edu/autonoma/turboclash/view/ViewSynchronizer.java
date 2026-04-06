package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.*;

import java.util.ArrayList;
import java.util.List;

public class ViewSynchronizer {
    private final List<Car> carBuffer = new ArrayList<>();

    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();

        window.updateScore(localPlayer.getCurrentPoints());
        window.updateHealth(localPlayer.getLives(), localPlayer.getCar());
        prepareCarList(localPlayer, match.getRemotePlayers());
        window.updateCars(carBuffer);
        window.updateObstacles(obstacles);
        window.updateItems(items);

        if (window.panel1 != null) {
            window.panel1.repaint();
        }
    }

    private void prepareCarList(Player local, List<Player> remotes) {
        carBuffer.clear();
        if (local.getCar() != null) carBuffer.add(local.getCar());
        if (remotes != null) {
            for (Player p : remotes) {
                if (p.getCar() != null) carBuffer.add(p.getCar());
            }
        }
    }
}