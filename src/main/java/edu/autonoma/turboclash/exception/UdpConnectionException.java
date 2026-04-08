package edu.autonoma.turboclash.exception;

/**
 * Representa la responsabilidad de {@code UdpConnectionException} en el manejo de errores.
 */
public class UdpConnectionException extends RuntimeException {

    /**
     * Crea una nueva instancia de {@code UdpConnectionException}.
     *
     * @param message valor del parametro {@code message}
     */
    public UdpConnectionException(String message) {

        super(message);

    }
}
