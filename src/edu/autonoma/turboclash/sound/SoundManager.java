package edu.autonoma.turboclash.sound;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {

    private Clip backgroundClip;
    private static final String BASE_PATH = "/edu/autonoma/turboclash/sound/";

    // Enum de sonidos (NO es variable quemada, es controlado)
    public enum Sound {
        COLLISION("CollisionSound.wav"),
        POINT("CoinSound.wav"),
        START("StartCountdownSound.wav"),
        END("GameOverSound.wav"),
        WIN("WinSound.wav"),
        BRAKE("BrakeSound.wav"),
        MENU("Game_Cover_Sound.wav");

        private final String fileName;

        Sound(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    // Música en loop
    public void playBackground(Sound sound) {
        try {
            stopBackground();

            InputStream input = getClass().getResourceAsStream(BASE_PATH + sound.getFileName());

            if (input == null) {
                System.err.println("No se encontró: " + sound.getFileName());
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
            InputStream input = getClass().getResourceAsStream(BASE_PATH + sound.getFileName());

            if (input == null) {
                System.err.println("No se encontró: " + sound.getFileName());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(input);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}