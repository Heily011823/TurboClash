package edu.autonoma.turboclash.validation;

public class PlayerCountValidator {

    public static void validate(int players) throws InvalidPlayerCountException {

        if (players < 2) {
            throw new InvalidPlayerCountException("Debe haber mínimo 2 jugadores");
        }

        if (players > 4) {
            throw new InvalidPlayerCountException("Solo son máximo 4 jugadores permitidos");
        }
    }
}