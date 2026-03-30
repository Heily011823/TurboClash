package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando el movimiento del jugador
 * se encuentra fuera de los límites permitidos.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.InvalidMovementException
 * @since 2026-03-29
 */
public class InvalidMovementException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message Es el mensaje de error asociado a la excepción.
     */
    public InvalidMovementException(String message) {

        super(message);

    }
}