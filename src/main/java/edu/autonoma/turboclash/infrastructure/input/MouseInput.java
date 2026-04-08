package edu.autonoma.turboclash.infrastructure.input;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.presentation.view.GameViewport;

public class MouseInput implements InputHandler {

    private int targetX, targetY;

    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    @Override
    public void update(Car car) {
        double dx = targetX - car.getX();
        double dy = targetY - car.getY();

        car.move(dx * 0.1, dy * 0.1);
        GameViewport.clampCar(car);
    }
}
