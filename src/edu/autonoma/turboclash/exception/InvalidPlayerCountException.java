package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando la cantidad de jugadores
 * no cumple con los límites establecidos.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidPlayerCountException
 * @since 2026-03-29
 */
public class InvalidPlayerCountException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error asociado a la excepción.
     */
    public InvalidPlayerCountException(String message) {

        super(message);

    }
}