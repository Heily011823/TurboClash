package edu.autonoma.turboclash.view;

import javax.swing.*;

import edu.autonoma.turboclash.sound.IAudioService;
import edu.autonoma.turboclash.sound.SoundManager;
import edu.autonoma.turboclash.navigation.IstartWindowListener;
public class StartWindow {
    public JPanel panel1;
    private JButton btnJugar;
    private final IstartWindowListener listener;
    private final IAudioService audioService;



    public StartWindow(IstartWindowListener listener, IAudioService audioService) {
        this.audioService = audioService;
        this.listener = listener;

        audioService.playMenuMusic();

        btnJugar.addActionListener(e -> listener.onPlayPressed());
    }
}