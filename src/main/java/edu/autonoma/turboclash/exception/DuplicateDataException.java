package edu.autonoma.turboclash.exception;

/**
 * Representa la responsabilidad de {@code DuplicateDataException} en el manejo de errores.
 */
public class DuplicateDataException extends Exception {

    /**
     * Crea una nueva instancia de {@code DuplicateDataException}.
     *
     * @param message valor del parametro {@code message}
     */
    public DuplicateDataException(String message) {

        super(message);

    }
}
