package edu.autonoma.turboclash.logic;
import model.Car;
import model.Item;
import model.Obstacle;

import java.util.Iterator;
import java.util.List;

public class CollisionManager {

    // Detecta colisión con item
    public boolean checkCarItemCollision(Car car, Item item) {
        if (car == null || item == null || !item.isVisible()) return false;
        return car.checkCollision(item);
    }

    // Detecta colisión con obstáculo
    public boolean checkCarObstacleCollision(Car car, Obstacle obstacle) {
        if (car == null || obstacle == null || !obstacle.isVisible()) return false;
        return car.checkCollision(obstacle);
    }

    // Procesa colisiones con items
    public Item processItemCollision(Car car, List<Item> items) {
        if (car == null || items == null) return null;

        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (checkCarItemCollision(car, item)) {
                item.setVisible(false);
                it.remove();
                return item;
            }
        }
        return null;
    }

    // Procesa colisiones con obstáculos
    public Obstacle processObstacleCollision(Car car, List<Obstacle> obstacles) {
        if (car == null || obstacles == null) return null;

        for (Obstacle o : obstacles) {
            if (checkCarObstacleCollision(car, o)) {
                return o;
            }
        }
        return null;
    }
}