package edu.autonoma.turboclash.input;

import edu.autonoma.turboclash.model.Car;

public class MouseInput implements InputHandler {

    private int targetX, targetY;

    public void setTarget(int x, int y) {
        this.targetX = x;
        this.targetY = y;
    }

    @Override
    public void update(Car car) {
        double dx = targetX - car.getPosX();
        double dy = targetY - car.getPosY();

        car.move(dx * 0.1, dy * 0.1);
    }
}