package edu.autonoma.turboclash.logic;
public class TimeManager {

    // Tiempo en milisegundos
    private long startTime;
    private long currentTime;
    private long gameDuration;
    private boolean started;

    public TimeManager(long gameDuration) {
        // Validamos que la duración sea válida
        if (gameDuration <= 0) {
            throw new IllegalArgumentException("La duración del juego debe ser mayor que cero.");
        }
        this.gameDuration = gameDuration;
        this.started = false;
    }

    // Inicia el contador de tiempo
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.currentTime = this.startTime;
        this.started = true;
    }

    // Actualiza el tiempo actual
    public void update() {
        if (!started) return;
        this.currentTime = System.currentTimeMillis();
    }

    // Retorna el tiempo transcurrido
    public long getElapsedTime() {
        if (!started) return 0;
        return currentTime - startTime;
    }

    // Retorna el tiempo restante
    public long getRemainingTime() {
        long remaining = gameDuration - getElapsedTime();
        return Math.max(remaining, 0);
    }

    // Verifica si el tiempo se acabó
    public boolean isTimeUp() {
        return getElapsedTime() >= gameDuration;
    }
}