package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidNameException;


/**
 * Representa la clase `PlayerNameValidator` y define su responsabilidad dentro del sistema.
 * @author Maria Paz Puerta Acevedo </mariap.puertaa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class PlayerNameValidator {

    /**
     * Ejecuta la operacion publica `validate`.
     * @param name valor del parametro `name`
     * @throws InvalidNameException se propaga si ocurre una condicion excepcional durante la ejecucion
     */
    public static void validate(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Debes ingresar un nombre.");
        }

        if (name.trim().length() < 3) {
            throw new InvalidNameException("El nombre debe tener al menos 3 caracteres.");
        }
    }
}
