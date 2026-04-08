package edu.autonoma.turboclash.infrastructure.sound;

/**
 * Define el contrato de servicio para {@code IAudioService} en la infraestructura de audio.
 */
public interface IAudioService {

    /**
     * Reproduce {@code MenuMusic}.
     */
    void playMenuMusic();

    /**
     * Reproduce {@code CoinSound}.
     */
    void playCoinSound();

    /**
     * Reproduce {@code BrakeSound}.
     */
    void playBrakeSound();

    /**
     * Detiene {@code Music}.
     */
    void stopMusic();

}
