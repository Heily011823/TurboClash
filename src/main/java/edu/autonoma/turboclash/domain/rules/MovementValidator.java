package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidMovementException;


/**
 * Valida las reglas asociadas a {@code MovementValidator} en las reglas del dominio.
 */
public class MovementValidator {

    /**
     * Valida las coordenadas del movimiento dentro de los límites del mapa.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param maxX Límite máximo en X
     * @param maxY Límite máximo en Y
     * @throws InvalidMovementException Se lanza la excepción si el movimiento está fuera del mapa
     */
    public static void validate(double x, double y, double maxX, double maxY)
            throws InvalidMovementException {

        if (x < 0 || y < 0 || x > maxX || y > maxY) {
            throw new InvalidMovementException("El movimiento está fuera del mapa");
        }
    }
}
