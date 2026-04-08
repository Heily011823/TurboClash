package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.presentation.view.GameViewport;

import java.util.List;

public class LanePositioner {

    public void placeCarsAtStart(List<Car> cars) {
        for (int i = 0; i < cars.size() && i < GameViewport.LANE_Y.length; i++) {
            Car car = cars.get(i);
            car.setPosition(GameViewport.CAR_START_X, GameViewport.laneY(i));
        }
    }
}