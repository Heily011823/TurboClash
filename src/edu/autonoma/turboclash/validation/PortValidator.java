package edu.autonoma.turboclash.validation;


/**
 * Clase encargada de validar puertos de conexión.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.PortValidator
 * @since 2026-03-29
 */
public class PortValidator {

    /**
     * Valida un número de puerto.
     *
     * @param port Es el número de puerto a validar.
     * @throws InvalidPortException Se lanza la excepción si el puerto está fuera del rango permitido.
     */
    public static void validate(int port) throws InvalidPortException {

        if (port < 1024 || port > 65535) {
            throw new InvalidPortException("El puerto está fuera de rango");
        }
    }
}