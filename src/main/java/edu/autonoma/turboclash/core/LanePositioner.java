package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Car;

import java.util.List;

public class LanePositioner {

    private static final int START_X = 80;
    private static final int[] LANES_Y = {100, 200, 300, 400};

    public void placeCarsAtStart(List<Car> cars) {
        for (int i = 0; i < cars.size() && i < LANES_Y.length; i++) {
            Car car = cars.get(i);
            car.setPosition(START_X, LANES_Y[i]);
        }
    }
}