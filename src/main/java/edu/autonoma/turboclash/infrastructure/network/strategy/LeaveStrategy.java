package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
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

    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
    @Override
    public void handle(GameMessage message) {
        if (message == null || match == null) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();
        String messagePlayerId = message.getPlayerId();
        String messagePlayerName = message.getPlayerName();

        if ((messagePlayerId == null || messagePlayerId.isBlank())
                && (messagePlayerName == null || messagePlayerName.isBlank())) {
            return;
        }

        // No eliminar al jugador local
        if (localPlayer != null) {
            boolean sameAsLocalById =
                    localPlayer.getId() != null
                            && messagePlayerId != null
                            && localPlayer.getId().equals(messagePlayerId);

            boolean sameAsLocalByName =
                    localPlayer.getName() != null
                            && messagePlayerName != null
                            && localPlayer.getName().equalsIgnoreCase(messagePlayerName);

            if (sameAsLocalById || sameAsLocalByName) {
                return;
            }
        }

        match.markPlayerRemoved(messagePlayerId, messagePlayerName);

        match.getRemotePlayers().removeIf(player -> {
            if (player == null) {
                return false;
            }

            boolean sameId =
                    player.getId() != null
                            && messagePlayerId != null
                            && player.getId().equals(messagePlayerId);

            boolean sameName =
                    player.getName() != null
                            && messagePlayerName != null
                            && player.getName().equalsIgnoreCase(messagePlayerName);

            return sameId || sameName;
        });
    }
}
