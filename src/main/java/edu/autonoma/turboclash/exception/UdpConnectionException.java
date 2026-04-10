package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `UdpConnectionException` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class UdpConnectionException extends RuntimeException {

    /**
     * Crea una nueva instancia de `UdpConnectionException`.
     * @param message valor del parametro `message`
     */
    public UdpConnectionException(String message) {

        super(message);

    }
}
