package edu.autonoma.turboclash.input;

import edu.autonoma.turboclash.model.Car;

public class KeyboardInput implements InputHandler {

    private boolean up, down, left, right;

    public void setUp(boolean up) { this.up = up; }
    public void setDown(boolean down) { this.down = down; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }

    @Override
    public void update(Car car) {
        int speed = 5;

        if (up) car.move(0, -speed);
        if (down) car.move(0, speed);
        if (left) car.move(-speed, 0);
        if (right) car.move(speed, 0);
    }
}