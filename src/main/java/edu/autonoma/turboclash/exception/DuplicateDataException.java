package edu.autonoma.turboclash.exception;

/**
 * Excepción que se lanza cuando se detectan datos duplicados
 * dentro del sistema.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.DuplicateDataException
 * @since 2026-03-30
 */
public class DuplicateDataException extends Exception {

    /**
     * Constructor de la excepción.
     *
     * @param message mensaje de error asociado a la excepción.
     */
    public DuplicateDataException(String message) {

        super(message);

    }
}