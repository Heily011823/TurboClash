package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;

public class RuleService {

    private static final double DEAD_ZONE_X = 0;

    public void checkPlayerOut(Match match) {
        Player local = match.getLocalPlayer();

        if (local == null || local.getCar() == null) return;

        if (local.getCar().getX() <= DEAD_ZONE_X) {
            match.setFinished(null);
        }
    }
}
