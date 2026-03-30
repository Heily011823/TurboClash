package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando el nombre del jugador es inválido.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidNameException
 * @since 2026-03-29
 */
public class InvalidNameException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error.
     */
    public InvalidNameException(String message) {

        super(message);
    }
}
