package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidPortException;


/**
 * Valida las reglas asociadas a {@code PortValidator} en las reglas del dominio.
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
