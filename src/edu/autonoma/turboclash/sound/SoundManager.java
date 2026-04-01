package edu.autonoma.turboclash.sound;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    // Rutas de sonidos del juego
    private String collisionSound;
    private String pointSound;
    private String startSound;
    private String endSound;
    private String winSound;

    public SoundManager(String collisionSound, String pointSound, String startSound, String endSound, String winSound) {
        this.collisionSound = collisionSound;
        this.pointSound = pointSound;
        this.startSound = startSound;
        this.endSound = endSound;
        this.winSound = winSound;
    }

    // Métodos para reproducir eventos del juego
    public void playCollision() {
        play(collisionSound);
    }

    public void playPoint() {
        play(pointSound);
    }

    public void playStart() {
        play(startSound);
    }

    public void playEnd() {
        play(endSound);
    }

    public void playWin() {
        play(winSound);
    }

    // Método genérico para reproducir audio
    private void play(String path) {
        if (path == null || path.isBlank()) return;

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error reproduciendo sonido: " + path);
        }
    }
}