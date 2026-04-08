package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public class ScoreStrategy implements IMessageStrategy {

    private final Match match;

    public ScoreStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {

        for (Player p : match.getPlayers()) {

            if (p.getId().equals(message.getPlayerId())) {

                p.setScore(message.getScore());
                break;
            }
        }
    }
}