package edu.autonoma.turboclash.infrastructure.sound;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa la clase `SoundManager` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class SoundManager implements IAudioService {

    private static SoundManager instance;
    private Clip backgroundClip;
    private static final String BASE_PATH = "/sound/";
    private final Map<Sound, Clip> effectClips = new HashMap<>();

    @Override
    /**
     * Reproduce el recurso asociado a play menu music.
     */
    public void playMenuMusic() {
        playBackground(Sound.MENU);
    }

    @Override
    /**
     * Reproduce el recurso asociado a play coin sound.
     */
    public void playCoinSound() {
        playEffect(Sound.POINT);
    }

    @Override
    /**
     * Reproduce el recurso asociado a play brake sound.
     */
    public void playBrakeSound() {
        playEffect(Sound.BRAKE);
    }

    @Override

    /**
     * Reproduce el recurso asociado a play countdown sound.
     */
    public void playCountdownSound() {
        playEffect(Sound.START);
    }

    @Override
    /**
     * Reproduce el recurso asociado a play win sound.
     */
    public void playWinSound() {
        playEffect(Sound.WIN);
    }

    /**
     * Detiene el flujo asociado a stop music.
     */
    public void stopMusic() {
        stopBackground();
    }

    // Enum de sonidos
    /**
     * Enumera las opciones disponibles para `Sound` dentro del sistema.
     *
     * @author 
     * @version 1.0
     */
    public enum Sound {
        COLLISION("CollisionSound.wav"),
        POINT("CoinSound.wav"),
        START("StartCountdownSound.wav"),
        END("GameOverSound.wav"),
        WIN("WinSound.wav"),
        BRAKE("BrakeSound.wav"),
        MENU("GameCoverSound.wav");

        private final String fileName;

        Sound(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Obtiene el valor asociado a `getFileName`.
         * @return resultado de la operacion documentada
         */
        public String getFileName() {
            return fileName;
        }
    }

    private SoundManager() {}

    /**
     * Obtiene el valor asociado a `getInstance`.
     * @return resultado de la operacion documentada
     */
    public static SoundManager getInstance() {
        if (instance == null) {
                instance = new SoundManager();
        }
        return instance;
    }

    // MÃºsica en loop
    /**
     * Reproduce el recurso asociado a play background.
     * @param sound valor del parametro `sound`
     */
    public void playBackground(Sound sound) {
        try {
            stopBackground();

            InputStream input = getClass().getResourceAsStream(BASE_PATH + sound.getFileName());

            if (input == null) {
                System.err.println("No se encontrÃ³: " + BASE_PATH + sound.getFileName());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(input);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Detener mÃºsica
    /**
     * Detiene el flujo asociado a stop background.
     */
    public void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    // Efectos
    /**
     * Reproduce el recurso asociado a play effect.
     * @param sound valor del parametro `sound`
     */
    public void playEffect(Sound sound) {
        try {
            Clip clip = effectClips.get(sound);

            if (clip == null) {
                InputStream input = getClass().getResourceAsStream(BASE_PATH + sound.getFileName());
                if (input == null) return;
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(input);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                effectClips.put(sound, clip);
            }

            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
