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
     * @param otherName Es el nombre del otro jugador para evitar duplicados.
     * @throws InvalidNameException Se lanza la excepción si el nombre no cumple con las reglas establecidas.
     * @throws DuplicateDataException Se lanza la excepción si el nombre ya está en uso.
     */
    public static void validate(String name, String otherName) throws InvalidNameException, DuplicateDataException {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("El nombre no puede estar vacío");
        }

        if (name.length() < 3) {
            throw new InvalidNameException("El nombre debe tener al menos 3 caracteres");
        }

        if (!name.matches("[a-zA-Z0-9]+")) {
            throw new InvalidNameException("El nombre solo puede contener letras y números");
        }

        if (otherName != null && name.equalsIgnoreCase(otherName)) {
            throw new DuplicateDataException("El nombre ya está en uso por otro jugador");
        }

        if (name.length() > 15) {
            throw new InvalidNameException("El nombre no puede tener más de 15 caracteres");
        }
    }
}
