package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando el puerto no se encuentra
 * dentro del rango permitido.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidPortException
 * @since 2026-03-29
 */
public class InvalidPortException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error asociado a la excepción.
     */
    public InvalidPortException(String message) {

        super(message);

    }
}