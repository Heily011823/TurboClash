package edu.autonoma.turboclash.validation;

import edu.autonoma.turboclash.exception.InvalidMovementException;


/**
 * Clase encargada de validar los movimientos dentro del juego.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.MovementValidator
 * @since 2026-03-29
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