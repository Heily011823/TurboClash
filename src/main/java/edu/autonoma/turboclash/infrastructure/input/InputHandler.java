package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;

/**
 * Define el contrato de `InputHandler` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public interface InputHandler {
    void update(Car car);
}
