package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.GameObject;

import java.awt.Dimension;

/**
 * Representa y organiza la vista {@code GameViewport} en la capa de presentacion.
 */
public final class GameViewport {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    public static final int CAR_START_X = 120;
    public static final int[] LANE_Y = {150, 280, 410, 540};

    public static final int ITEM_SPAWN_X = WIDTH + 50;
    public static final int OBSTACLE_SPAWN_X = WIDTH + 50;
    public static final int WORLD_TOP_MARGIN = 120;
    public static final int WORLD_BOTTOM_MARGIN = 120;

    /**
     * Crea una nueva instancia de {@code GameViewport}.
     */
    private GameViewport() {
    }

    /**
     * Ejecuta la operacion {@code size}.
     *
     * @return resultado de la operacion {@code size}
     */
    public static Dimension size() {
        return new Dimension(WIDTH, HEIGHT);
    }

    /**
     * Ejecuta la operacion {@code clampX}.
     *
     * @param x coordenada horizontal utilizada en la operacion
     * @param object valor del parametro {@code object}
     * @return resultado de la operacion {@code clampX}
     */
    public static double clampX(double x, GameObject object) {
        int objectWidth = object != null ? object.getWidth() : 0;
        return Math.max(0, Math.min(x, WIDTH - objectWidth));
    }

    /**
     * Ejecuta la operacion {@code clampY}.
     *
     * @param y coordenada vertical utilizada en la operacion
     * @param object valor del parametro {@code object}
     * @return resultado de la operacion {@code clampY}
     */
    public static double clampY(double y, GameObject object) {
        int objectHeight = object != null ? object.getHeight() : 0;
        return Math.max(0, Math.min(y, HEIGHT - objectHeight));
    }

    /**
     * Ejecuta la operacion {@code clampCar}.
     *
     * @param car valor del parametro {@code car}
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
     * Ejecuta la operacion {@code laneY}.
     *
     * @param index valor del parametro {@code index}
     * @return resultado de la operacion {@code laneY}
     */
    public static int laneY(int index) {
        return LANE_Y[Math.max(0, Math.min(index, LANE_Y.length - 1))];
    }

    /**
     * Ejecuta la operacion {@code randomPlayableY}.
     *
     * @param objectHeight valor del parametro {@code objectHeight}
     * @param random valor del parametro {@code random}
     * @return resultado de la operacion {@code randomPlayableY}
     */
    public static int randomPlayableY(int objectHeight, java.util.Random random) {
        int minY = WORLD_TOP_MARGIN;
        int maxY = HEIGHT - WORLD_BOTTOM_MARGIN - objectHeight;
        return minY + random.nextInt(Math.max(1, maxY - minY + 1));
    }
}
