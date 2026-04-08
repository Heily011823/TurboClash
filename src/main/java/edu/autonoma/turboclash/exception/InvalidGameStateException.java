package edu.autonoma.turboclash.exception;

/**
 * Representa la responsabilidad de {@code InvalidGameStateException} en el manejo de errores.
 */
public class InvalidGameStateException extends Exception {

    /**
     * Crea una nueva instancia de {@code InvalidGameStateException}.
     *
     * @param message valor del parametro {@code message}
     */
    public InvalidGameStateException(String message) {

        super(message);

    }
}
