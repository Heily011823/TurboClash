package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `InvalidMovementException` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class InvalidMovementException extends Exception {

    /**
     * Crea una nueva instancia de `InvalidMovementException`.
     * @param message valor del parametro `message`
     */
    public InvalidMovementException(String message) {

        super(message);

    }
}
