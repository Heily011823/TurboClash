package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `InvalidNameException` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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
