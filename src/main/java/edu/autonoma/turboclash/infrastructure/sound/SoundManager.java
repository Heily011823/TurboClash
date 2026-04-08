package edu.autonoma.turboclash.infrastructure.sound;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundManager implements IAudioService {

    private static SoundManager instance;
    private Clip backgroundClip;
    private static final String BASE_PATH = "/sound/";
    private final Map<Sound, Clip> effectClips = new HashMap<>();

    @Override
    public void playMenuMusic() {
        playBackground(Sound.MENU);
    }

    @Override
    public void playCoinSound() {
        playEffect(Sound.POINT);
    }

    @Override
    public void playBrakeSound() {
        playEffect(Sound.BRAKE);
    }

    @Override
    public void playCountdownSound() {
        playEffect(Sound.START);
    }

    @Override
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

    public static SoundManager getInstance() {
        if (instance == null) {
                instance = new SoundManager();
        }
        return instance;
    }

    // Música en loop
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
    public void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }

    // Efectos
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