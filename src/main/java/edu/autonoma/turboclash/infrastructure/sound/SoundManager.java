package edu.autonoma.turboclash.infrastructure.sound;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Administra la responsabilidad principal de {@code SoundManager} en la infraestructura de audio.
 */
public class SoundManager implements IAudioService {

    private static SoundManager instance;
    private Clip backgroundClip;
    private static final String BASE_PATH = "/sound/";
    private final Map<Sound, Clip> effectClips = new HashMap<>();

    @Override
    /**
     * Reproduce {@code MenuMusic}.
     */
    public void playMenuMusic() {
        playBackground(Sound.MENU);
    }

    @Override
    /**
     * Reproduce {@code CoinSound}.
     */
    public void playCoinSound() {
        playEffect(Sound.POINT);
    }

    @Override
    /**
     * Reproduce {@code BrakeSound}.
     */
    public void playBrakeSound() {
        playEffect(Sound.BRAKE);
    }

    @Override
    /**
     * Detiene {@code Music}.
     */

    public void playCountdownSound() {
        playEffect(Sound.START);
    }

    @Override
    public void playWinSound() {
        playEffect(Sound.WIN);
    }

    public void stopMusic() {
        stopBackground();
    }

    // Enum de sonidos
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

        public String getFileName() {
            return fileName;
        }
    }

    private SoundManager() {}

    /**
     * Obtiene el valor de {@code Instance}.
     *
     * @return valor de {@code Instance}
     */
    public static SoundManager getInstance() {
        if (instance == null) {
                instance = new SoundManager();
        }
        return instance;
    }

    // Música en loop
    /**
     * Reproduce {@code Background}.
     *
     * @param sound valor del parametro {@code sound}
     */
    public void playBackground(Sound sound) {
        try {
            stopBackground();

            InputStream input = getClass().getResourceAsStream(BASE_PATH + sound.getFileName());

            if (input == null) {
                System.err.println("No se encontró: " + BASE_PATH + sound.getFileName());
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

    // Detener música
    /**
     * Detiene {@code Background}.
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
     * Reproduce {@code Effect}.
     *
     * @param sound valor del parametro {@code sound}
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
