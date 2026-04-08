package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code LeaveStrategy} en las estrategias de mensajeria.
 */
public class LeaveStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de {@code LeaveStrategy}.
     *
     * @param match valor del parametro {@code match}
     */
    public LeaveStrategy(Match match) {
        this.match = match;
    }

    @Override
    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
    public void handle(GameMessage message) {


        if (message.getPlayerId().equals(match.getLocalPlayer().getId())) {
            return;
        }

        match.getRemotePlayers()
                .removeIf(p -> p.getId().equals(message.getPlayerId()));

        System.out.println("Jugador eliminado: " + message.getPlayerId());
    }
}
