package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.presentation.view.GameViewport;

/**
 * Representa la clase `MouseInput` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class MouseInput implements InputHandler {

    private int targetX, targetY;

    /**
     * Actualiza el valor asociado a `setTarget`.
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     */
    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    @Override
    /**
     * Ejecuta la operacion publica `update`.
     * @param car valor del parametro `car`
     */
    public void update(Car car) {
        double dx = targetX - car.getX();
        double dy = targetY - car.getY();

        car.move(dx * 0.1, dy * 0.1);
        GameViewport.clampCar(car);
    }
}
