package edu.autonoma.turboclash.validation;

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
     * Valida las coordenadas del movimiento.
     *
     * @param x Es la coordenada en el eje X.
     * @param y Es la coordenada en el eje Y.
     * @throws InvalidMovementException Se lanza la excepción si el movimiento está fuera de los límites permitidos.
     */
    public static void validate(double x, double y) throws InvalidMovementException {

        if (x < 0 || y < 0) {
            throw new InvalidMovementException("El movimiento esta fuera del mapa");
        }
    }
}