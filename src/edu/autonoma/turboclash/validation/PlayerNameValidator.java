package edu.autonoma.turboclash.validation;

public class PlayerNameValidator {

    public static void validate(String name) throws InvalidNameException {

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
            throw new InvalidNameException("El nombre ya está en uso por otro jugador");
        }

        if (name.length() > 15) {
            throw new InvalidNameException("El nombre no puede tener más de 15 caracteres");
        }
    }
}
