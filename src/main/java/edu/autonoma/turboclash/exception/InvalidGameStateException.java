package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `InvalidGameStateException` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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
