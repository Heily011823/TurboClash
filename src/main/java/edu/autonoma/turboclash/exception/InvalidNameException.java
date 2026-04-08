package edu.autonoma.turboclash.exception;

/**
 * Representa la responsabilidad de {@code InvalidNameException} en el manejo de errores.
 */
public class InvalidNameException extends Exception {

    /**
     * Crea una nueva instancia de {@code InvalidNameException}.
     *
     * @param message valor del parametro {@code message}
     */
    public InvalidNameException(String message) {

        super(message);
    }
}
