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
    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
    public void handle(GameMessage message) {

        for (Player p : match.getPlayers()) {

            if (p.getId().equals(message.getPlayerId())) {

                p.setScore(message.getScore());
                break;
            }
        }
    }
}
