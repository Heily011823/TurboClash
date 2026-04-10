package edu.autonoma.turboclash.exception;

/**
 * Representa la clase `UdpConnectionException` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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
