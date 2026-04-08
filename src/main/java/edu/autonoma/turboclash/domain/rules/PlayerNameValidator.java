package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidNameException;


/**
 * Valida las reglas asociadas a {@code PlayerNameValidator} en las reglas del dominio.
 */
public class PlayerNameValidator {

    /**
     * Valida el nombre de un jugador.
     *
     * @param name Es el nombre del jugador a validar.
     * @throws InvalidNameException Se lanza la excepción si el nombre no cumple con las reglas establecidas.

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
