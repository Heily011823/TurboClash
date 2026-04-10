package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidGameStateException;


/**
 * Representa la clase `GameStateValidator` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameStateValidator {

    /**
     * Valida las condiciones asociadas a validate start.
     * @param playersConnected valor del parametro `playersConnected`
     * @throws InvalidGameStateException se propaga si ocurre una condicion excepcional durante la ejecucion
     */
    public static void validateStart(boolean playersConnected) throws InvalidGameStateException {

        if (!playersConnected) {
            throw new InvalidGameStateException("No se puede iniciar el juego sin los 4 jugadores");
        }
    }
}
