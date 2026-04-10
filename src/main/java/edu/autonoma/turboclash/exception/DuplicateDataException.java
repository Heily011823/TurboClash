package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `DuplicateDataException` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class DuplicateDataException extends Exception {

    /**
     * Crea una nueva instancia de `DuplicateDataException`.
     * @param message valor del parametro `message`
     */
    public DuplicateDataException(String message) {

        super(message);

    }
}
