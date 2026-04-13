package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la clase `LeaveStrategy` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class LeaveStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de `LeaveStrategy`.
     * @param match valor del parametro `match`
     */
    public LeaveStrategy(Match match) {
        this.match = match;
    }

    @Override
    /**
     * Ejecuta la operacion publica `handle`.
     * @param message valor del parametro `message`
     */
    public void handle(GameMessage message) {
        if (message == null || match == null) {
            return;
        }

        if (match.isFinished()) {
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
