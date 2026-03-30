package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando la dirección IP no cumple
 * con el formato válido establecido.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidIpException
 * @since 2026-03-29
 */
public class InvalidIpException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error asociado a la excepción
     */
    public InvalidIpException(String message) {

        super(message);

    }
}