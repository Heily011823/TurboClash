package edu.autonoma.turboclash.logic;



public class GameConstants {

    // --- REGLAS DE PUNTAJE (Reglas 1 y 2) ---
    /** Puntos ganados al recoger una moneda */
    public static final int COIN_VALUE = 20;

    /** Puntos perdidos al chocar con un obstáculo */
    public static final int OBSTACLE_PENALTY = 10;

    /** Puntos perdidos al chocar contra otro jugador */
    public static final int COLLISION_PENALTY = 10;


    // --- REGLAS DE SALUD (Regla 3) ---
    /** Vidas (corazones) iniciales de cada jugador */
    public static final int INITIAL_LIVES = 3;


    // --- REGLAS DE MOVIMIENTO (Regla 4) ---
    /** Factor de reducción de velocidad (0.5 = 50% de la velocidad normal) */
    public static final double DEBUFF_SPEED_FACTOR = 0.5;

    /** Duración del efecto de lentitud tras un choque en milisegundos */
    public static final long DEBUFF_DURATION_MS = 2000;


    // --- REGLAS DE VICTORIA (Regla 5) ---
    /** Puntaje por defecto para ganar si no se llega a la meta física */
    public static final int DEFAULT_TARGET_SCORE = 100;

    /** Bono de puntos extra por llegar primero a la meta */
    public static final int FINISH_LINE_BONUS = 50;


    // --- CONFIGURACIÓN DE OBJETOS ---
    /** Ancho estándar de los vehículos */
    public static final int CAR_WIDTH = 40;

    /** Alto estándar de los vehículos */
    public static final int CAR_HEIGHT = 20;
}