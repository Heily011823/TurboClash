package edu.autonoma.turboclash.domain.services;



/**
 * Representa la clase `GameConstants` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameConstants {

    // --- REGLAS DE PUNTAJE (Reglas 1 y 2) ---
    /**
     * Expone el atributo publico `COIN_VALUE` para la colaboracion entre componentes del sistema.
     */
    public static final int COIN_VALUE = 20;

    /**
     * Expone el atributo publico `COLLISION_COOLDOWN` para la colaboracion entre componentes del sistema.
     */
    public static final long COLLISION_COOLDOWN = 100;
    /**
     * Expone el atributo publico `OBSTACLE_PENALTY` para la colaboracion entre componentes del sistema.
     */
    public static final int OBSTACLE_PENALTY = 10;

    /**
     * Expone el atributo publico `COLLISION_PENALTY` para la colaboracion entre componentes del sistema.
     */
    public static final int COLLISION_PENALTY = 10;


    // --- REGLAS DE SALUD (Regla 3) ---
    /**
     * Expone el atributo publico `INITIAL_LIVES` para la colaboracion entre componentes del sistema.
     */
    public static final int INITIAL_LIVES = 3;


    // --- REGLAS DE MOVIMIENTO (Regla 4) ---
    /**
     * Expone el atributo publico `DEBUFF_SPEED_FACTOR` para la colaboracion entre componentes del sistema.
     */
    public static final double DEBUFF_SPEED_FACTOR = 0.5;

    /**
     * Expone el atributo publico `DEBUFF_DURATION_MS` para la colaboracion entre componentes del sistema.
     */
    public static final long DEBUFF_DURATION_MS = 2000;


    // --- REGLAS DE VICTORIA (Regla 5) ---
    /**
     * Expone el atributo publico `DEFAULT_TARGET_SCORE` para la colaboracion entre componentes del sistema.
     */
    public static final int DEFAULT_TARGET_SCORE = 100;

    /**
     * Expone el atributo publico `FINISH_LINE_BONUS` para la colaboracion entre componentes del sistema.
     */
    public static final int FINISH_LINE_BONUS = 50;


    // --- CONFIGURACIÃ“N DE OBJETOS ---
    /**
     * Expone el atributo publico `CAR_WIDTH` para la colaboracion entre componentes del sistema.
     */
    public static final int CAR_WIDTH = 40;

    /**
     * Expone el atributo publico `CAR_HEIGHT` para la colaboracion entre componentes del sistema.
     */
    public static final int CAR_HEIGHT = 20;
}
