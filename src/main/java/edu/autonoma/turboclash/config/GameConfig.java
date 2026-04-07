package edu.autonoma.turboclash.config;

public class GameConfig {
    // 🎮 PUERTOS (multijugador)
    public static final int MIN_PORT = 5000;
    public static final int MAX_PORT = 5003;


    private static final int FRAME_DELAY = 16; // ~60 FPS


    public static final int INITIAL_LIVES = 3;
    public static final int TARGET_SCORE = 10;


    public static final long COLLISION_COOLDOWN = 1000; // ms


    public static final String DEFAULT_HOST = "localhost";


    public static int getFrameDelay() {
        return FRAME_DELAY;
    }

    public static boolean isValidPort(int puerto) {
        return puerto >= MIN_PORT && puerto <= MAX_PORT;
    }
}
