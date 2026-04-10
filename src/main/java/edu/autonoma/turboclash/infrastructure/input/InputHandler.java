package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;

/**
 * Define el contrato de `InputHandler` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface InputHandler {
    void update(Car car);
}
