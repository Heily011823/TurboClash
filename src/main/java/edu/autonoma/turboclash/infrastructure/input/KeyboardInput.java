package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.presentation.view.GameViewport;

/**
 * Representa la clase `KeyboardInput` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class KeyboardInput implements InputHandler {

    private boolean up, down, left, right;

    /**
     * Actualiza el valor asociado a `setUp`.
     * @param up valor del parametro `up`
     */
    public void setUp(boolean up) { this.up = up; }
    /**
     * Actualiza el valor asociado a `setDown`.
     * @param down valor del parametro `down`
     */
    public void setDown(boolean down) { this.down = down; }
    /**
     * Actualiza el valor asociado a `setLeft`.
     * @param left valor del parametro `left`
     */
    public void setLeft(boolean left) { this.left = left; }
    /**
     * Actualiza el valor asociado a `setRight`.
     * @param right valor del parametro `right`
     */
    public void setRight(boolean right) { this.right = right; }

    @Override
    /**
     * Ejecuta la operacion publica `update`.
     * @param car valor del parametro `car`
     */
    public void update(Car car) {
        double dx = 0;
        double dy = 0;

        if (left) dx -= 1;
        if (right) dx += 1;
        if (up) dy -= 1;
        if (down) dy += 1;

        car.move(dx, dy);
        GameViewport.clampCar(car);
    }
}
