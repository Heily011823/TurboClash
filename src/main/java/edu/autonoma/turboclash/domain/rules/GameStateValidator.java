package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidGameStateException;


/**
 * Valida las reglas asociadas a {@code GameStateValidator} en las reglas del dominio.
 */
public class GameStateValidator {

    /**
     * Valida si la partida puede iniciar.
     *
     * @param playersConnected Indica si los jugadores están conectados.
     * @throws InvalidGameStateException Se lanza la excepción si no hay jugadores conectados.
     */
    public static void validateStart(boolean playersConnected) throws InvalidGameStateException {

        if (!playersConnected) {
            throw new InvalidGameStateException("No se puede iniciar el juego sin los 4 jugadores");
        }
    }
}
