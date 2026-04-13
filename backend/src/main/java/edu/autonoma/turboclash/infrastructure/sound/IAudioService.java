package edu.autonoma.turboclash.infrastructure.sound;

/**
 * Define el contrato de `IAudioService` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface IAudioService {

    void playMenuMusic();

    void playCoinSound();

    void playBrakeSound();

    void playCountdownSound();

    void playWinSound();

    void stopMusic();

}
