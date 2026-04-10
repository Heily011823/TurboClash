package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidNameException;


/**
 * Representa la clase `PlayerNameValidator` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
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
