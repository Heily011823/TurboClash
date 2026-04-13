package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `InvalidNameException` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class InvalidNameException extends Exception {

    /**
     * Crea una nueva instancia de `InvalidNameException`.
     * @param message valor del parametro `message`
     */
    public InvalidNameException(String message) {

        super(message);
    }
}
