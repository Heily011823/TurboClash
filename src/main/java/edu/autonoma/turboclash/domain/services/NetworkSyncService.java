package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public class NetworkSyncService {

    public void syncPlayer(Match match, GameMessage msg) {
        if (msg == null) return;

        for (Player p : match.getPlayers()) {
            if (p.getId().equals(msg.getPlayerId())) {
                p.syncFromNetwork(msg.getPosX(), msg.getPosY(), msg.getScore());
            }
        }
    }
}
