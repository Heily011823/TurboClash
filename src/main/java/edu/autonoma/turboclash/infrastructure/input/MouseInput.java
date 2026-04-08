package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.presentation.view.GameViewport;

/**
 * Representa la responsabilidad de {@code MouseInput} en la gestion de entrada.
 */
public class MouseInput implements InputHandler {

    private int targetX, targetY;

    /**
     * Actualiza el valor de {@code Target}.
     *
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     */
    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    @Override
    /**
     * Actualiza la operacion principal del metodo.
     *
     * @param car valor del parametro {@code car}
     */
    public void update(Car car) {
        double dx = targetX - car.getX();
        double dy = targetY - car.getY();

        car.move(dx * 0.1, dy * 0.1);
        GameViewport.clampCar(car);
    }
}
