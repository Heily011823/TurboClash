package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando se intenta realizar una acción
 * en un estado inválido de la partida.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidGameStateException
 * @since 2026-03-29
 */
public class InvalidGameStateException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error asociado a la excepción.
     */
    public InvalidGameStateException(String message) {

        super(message);

    }
}