package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `InvalidGameStateException` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class InvalidGameStateException extends Exception {

    /**
     * Crea una nueva instancia de `InvalidGameStateException`.
     * @param message valor del parametro `message`
     */
    public InvalidGameStateException(String message) {

        super(message);

    }
}
