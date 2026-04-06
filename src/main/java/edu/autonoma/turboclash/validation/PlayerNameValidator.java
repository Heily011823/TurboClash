package edu.autonoma.turboclash.validation;

import edu.autonoma.turboclash.exception.InvalidNameException;
import edu.autonoma.turboclash.exception.DuplicateDataException;


/**
 * Clase encargada de validar el nombre del jugador.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.PlayerNameValidator
 * @since 2026-03-29
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
