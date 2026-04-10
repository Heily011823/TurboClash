package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code ScoreStrategy} en las estrategias de mensajeria.
 */
public class ScoreStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de {@code ScoreStrategy}.
     *
     * @param match valor del parametro {@code match}
     */
    public ScoreStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {

        for (Player p : match.getPlayers()) {

            boolean sameId =
                    p.getId() != null &&
                            message.getPlayerId() != null &&
                            p.getId().equals(message.getPlayerId());

            boolean sameName =
                    p.getName() != null &&
                            message.getPlayerName() != null &&
                            p.getName().equalsIgnoreCase(message.getPlayerName());

            if (sameId || sameName) {
                p.setScore(message.getScore());
                break;
            }
        }
    }
}
