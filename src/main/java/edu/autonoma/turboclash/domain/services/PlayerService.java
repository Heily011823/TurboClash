package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;

public class PlayerService {

    public void updatePlayers(Match match) {
        for (Player p : match.getPlayers()) {
            if (p.getCar() != null) {
                p.getCar().updateDebuff();
            }
        }
    }

    public void movePlayer(Player player, double dx, double dy) {
        if (player == null || player.getCar() == null) return;
        player.getCar().move(dx, dy);
    }
}
