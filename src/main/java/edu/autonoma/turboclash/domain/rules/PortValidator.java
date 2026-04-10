package edu.autonoma.turboclash.domain.rules;

import edu.autonoma.turboclash.exception.InvalidPortException;


/**
 * Representa la clase `PortValidator` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class PortValidator {

    /**
     * Ejecuta la operacion publica `validate`.
     * @param port valor del parametro `port`
     * @throws InvalidPortException se propaga si ocurre una condicion excepcional durante la ejecucion
     */
    public static void validate(int port) throws InvalidPortException {

        if (port < 1024 || port > 65535) {
            throw new InvalidPortException("El puerto estÃ¡ fuera de rango");
        }
    }
}
