package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Car;

import java.util.List;


public class LanePositioner {

    private final int startX;
    private final int[] lanesY;

    public LanePositioner(int startX, int[] lanesY) {
        this.startX = startX;
        this.lanesY = lanesY;
    }

    public void placeCarsAtStart(List<Car> cars) {
        for (int i = 0; i < cars.size() && i < lanesY.length; i++) {
            cars.get(i).setPosition(startX, lanesY[i]);
        }
    }
}