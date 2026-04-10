package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.GameObject;

import java.awt.Dimension;

/**
 * Representa la clase `GameViewport` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public final class GameViewport {

    /**
     * Expone el atributo publico `WIDTH` para la colaboracion entre componentes del sistema.
     */
    public static final int WIDTH = 1200;
    /**
     * Expone el atributo publico `HEIGHT` para la colaboracion entre componentes del sistema.
     */
    public static final int HEIGHT = 800;

    /**
     * Expone el atributo publico `CAR_START_X` para la colaboracion entre componentes del sistema.
     */
    public static final int CAR_START_X = 120;
    /**
     * Expone el atributo publico `LANE_Y` para la colaboracion entre componentes del sistema.
     */
    public static final int[] LANE_Y = {150, 280, 410, 540};

    /**
     * Expone el atributo publico `ITEM_SPAWN_X` para la colaboracion entre componentes del sistema.
     */
    public static final int ITEM_SPAWN_X = WIDTH + 50;
    /**
     * Expone el atributo publico `OBSTACLE_SPAWN_X` para la colaboracion entre componentes del sistema.
     */
    public static final int OBSTACLE_SPAWN_X = WIDTH + 50;
    /**
     * Expone el atributo publico `WORLD_TOP_MARGIN` para la colaboracion entre componentes del sistema.
     */
    public static final int WORLD_TOP_MARGIN = 120;
    /**
     * Expone el atributo publico `WORLD_BOTTOM_MARGIN` para la colaboracion entre componentes del sistema.
     */
    public static final int WORLD_BOTTOM_MARGIN = 120;

    private GameViewport() {
    }

    /**
     * Ejecuta la operacion publica `size`.
     * @return resultado de la operacion documentada
     */
    public static Dimension size() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * Ejecuta la operacion publica `clampX`.
     * @param x valor del parametro `x`
     * @param object valor del parametro `object`
     * @return resultado de la operacion documentada
     */
    public static double clampX(double x, GameObject object) {
        int objectWidth = object != null ? object.getWidth() : 0;
        return Math.max(0, Math.min(x, WIDTH - objectWidth));
    }

    /**
     * Ejecuta la operacion publica `clampY`.
     * @param y valor del parametro `y`
     * @param object valor del parametro `object`
     * @return resultado de la operacion documentada
     */
    public static double clampY(double y, GameObject object) {
        int objectHeight = object != null ? object.getHeight() : 0;
        return Math.max(0, Math.min(y, HEIGHT - objectHeight));
    }

    /**
     * Ejecuta la operacion publica `clampCar`.
     * @param car valor del parametro `car`
     */
    public static void clampCar(Car car) {
        if (car == null) {
            return;
        }

        car.setPosition(
                clampX(car.getX(), car),
                clampY(car.getY(), car)
        );
    }

    /**
     * Ejecuta la operacion publica `laneY`.
     * @param index valor del parametro `index`
     * @return resultado de la operacion documentada
     */
    public static int laneY(int index) {
        return LANE_Y[Math.max(0, Math.min(index, LANE_Y.length - 1))];
    }

    /**
     * Ejecuta la operacion publica `randomPlayableY`.
     * @param objectHeight valor del parametro `objectHeight`
     * @param random valor del parametro `random`
     * @return resultado de la operacion documentada
     */
    public static int randomPlayableY(int objectHeight, java.util.Random random) {
        int minY = WORLD_TOP_MARGIN;
        int maxY = HEIGHT - WORLD_BOTTOM_MARGIN - objectHeight;
        return minY + random.nextInt(Math.max(1, maxY - minY + 1));
    }
}
