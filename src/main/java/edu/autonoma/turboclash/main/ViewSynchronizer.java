package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.view.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ViewSynchronizer {

    public void sync(GameWindow window, Match match, List<Obstacle> obstacles) {

        Player local = match.getLocalPlayer();

        window.actualizarPuntaje(local.getCurrentPoints());
        window.actualizarCorazones(local.getLives(), local.getCar());

        List<Car> cars = new ArrayList<>();
        cars.add(local.getCar());
        match.getRemotePlayers().forEach(p -> cars.add(p.getCar()));

        window.actualizarCarros(cars);

        List<Point> puntos = new ArrayList<>();
        for (Obstacle o : obstacles) {
            puntos.add(new Point((int) o.getX(), (int) o.getY()));
        }

        window.actualizarObstaculos(puntos);
    }
}