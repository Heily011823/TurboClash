package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;

/**
 * Define el contrato de {@code InputHandler} en la gestion de entrada.
 */
public interface InputHandler {
    /**
     * Actualiza la operacion principal del metodo.
     *
     * @param car valor del parametro {@code car}
     */
    void update(Car car);
}
