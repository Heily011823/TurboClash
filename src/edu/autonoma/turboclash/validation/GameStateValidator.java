package edu.autonoma.turboclash.validation;

/**
 * Clase encargada de validar el estado de la partida.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.GameStateValidator
 * @since 2026-03-29
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
            throw new InvalidGameStateException("No se puede iniciar el juego sin jugadores");
        }
    }

    /**
     * Valida si la partida puede finalizar.
     *
     * @param gameStarted Indica si la partida ha iniciado.
     * @throws InvalidGameStateException Se lanza la excepción si la partida no ha comenzado.
     */
    public static void validateEnd(boolean gameStarted) throws InvalidGameStateException {

        if (!gameStarted) {
            throw new InvalidGameStateException("No se puede finalizar el juego sin iniciar");
        }
    }
}