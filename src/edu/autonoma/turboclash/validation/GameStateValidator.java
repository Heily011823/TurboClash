package edu.autonoma.turboclash.validation;

public class GameStateValidator {

    public static void validateStart(boolean playersConnected) throws InvalidGameStateException {

        if (!playersConnected) {
            throw new InvalidGameStateException("No se puede iniciar sin jugadores");
        }
    }

    public static void validateEnd(boolean gameStarted) throws InvalidGameStateException {

        if (!gameStarted) {
            throw new InvalidGameStateException("No se puede finalizar sin iniciar");
        }
    }
}