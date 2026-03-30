package edu.autonoma.turboclash.validation;

/**
 * Clase encargada de validar la cantidad de jugadores.
 *
 * @author María Paz Puerta Acevedo <mariap.puertaa@autonoma.edu.co>
 * @version 1.0.0
 * @see edu.autonoma.turboclash.validation.PlayerCountValidator
 * @since 2026-03-29
 */
public class PlayerCountValidator {

    /**
     * Valida la cantidad de jugadores.
     *
     * @param players Es el número de jugadores.
     * @throws InvalidPlayerCountException Se lanza la excepción si la cantidad es menor a 2 o mayor a 4.
     */
    public static void validate(int players) throws InvalidPlayerCountException {

        if (players < 2) {
            throw new InvalidPlayerCountException("Debe haber mínimo 2 jugadores");
        }

        if (players > 4) {
            throw new InvalidPlayerCountException("Solo son máximo 4 jugadores permitidos");
        }
    }
}