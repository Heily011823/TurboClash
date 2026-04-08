package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public class LeaveStrategy implements IMessageStrategy {

    private final Match match;

    public LeaveStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {


        if (message.getPlayerId().equals(match.getLocalPlayer().getId())) {
            return;
        }

        match.getRemotePlayers()
                .removeIf(p -> p.getId().equals(message.getPlayerId()));

        System.out.println("Jugador eliminado: " + message.getPlayerId());
    }
}