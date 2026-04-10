package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidMovementException;


/**
 * Representa la clase `MovementValidator` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class MovementValidator {

    /**
     * Ejecuta la operacion publica `validate`.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param maxX valor del parametro `maxX`
     * @param maxY valor del parametro `maxY`
     * @throws InvalidMovementException se propaga si ocurre una condicion excepcional durante la ejecucion
     */
    public static void validate(double x, double y, double maxX, double maxY)
            throws InvalidMovementException {

        if (x < 0 || y < 0 || x > maxX || y > maxY) {
            throw new InvalidMovementException("El movimiento estÃ¡ fuera del mapa");
        }
    }
}
