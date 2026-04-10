package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la clase `ScoreStrategy` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class ScoreStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de `ScoreStrategy`.
     * @param match valor del parametro `match`
     */
    public ScoreStrategy(Match match) {
        this.match = match;
    }

    @Override
    /**
     * Ejecuta la operacion publica `handle`.
     * @param message valor del parametro `message`
     */
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
