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

        double newX = car.getX();
        double newY = car.getY();

        if (up) newY -= speed;
        if (down) newY += speed;
        if (left) newX -= speed;
        if (right) newX += speed;

        car.setPosition(newX, newY);
    }
}