package edu.autonoma.turboclash.domain.services;



/**
 * Representa la responsabilidad de {@code GameConstants} en los servicios de dominio.
 */
public class GameConstants {

    // --- REGLAS DE PUNTAJE (Reglas 1 y 2) ---
    public static final int COIN_VALUE = 20;

    public static final long COLLISION_COOLDOWN = 100;
    public static final int OBSTACLE_PENALTY = 10;

    public static final int COLLISION_PENALTY = 10;


    // --- REGLAS DE SALUD (Regla 3) ---
    public static final int INITIAL_LIVES = 3;


    // --- REGLAS DE MOVIMIENTO (Regla 4) ---
    public static final double DEBUFF_SPEED_FACTOR = 0.5;

    public static final long DEBUFF_DURATION_MS = 2000;


    // --- REGLAS DE VICTORIA (Regla 5) ---
    public static final int DEFAULT_TARGET_SCORE = 100;

    public static final int FINISH_LINE_BONUS = 50;


    // --- CONFIGURACIÓN DE OBJETOS ---
    public static final int CAR_WIDTH = 40;

    public static final int CAR_HEIGHT = 20;
}
