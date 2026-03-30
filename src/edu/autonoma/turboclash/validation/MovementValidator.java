package edu.autonoma.turboclash.validation;

public class MovementValidator {

    public static void validate(double x, double y) throws InvalidMovementException {

        if (x < 0 || y < 0) {
            throw new InvalidMovementException("El movimiento esta fuera del mapa");
        }
    }
}