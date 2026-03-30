package edu.autonoma.turboclash.exception;

/**
 * Esta excepción se lanza cuando ocurre un error en la conexión
 * mediante el protocolo UDP.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.exception.UdpConnectionException
 * @since 2026-03-30
 */
public class UdpConnectionException extends RuntimeException {

    /**
     * Constructor de la excepción.
     *
     * @param message mensaje de error asociado a la excepción.
     */
    public UdpConnectionException(String message) {

        super(message);

    }
}