package edu.autonoma.turboclash.view;

import javax.swing.*;
import edu.autonoma.turboclash.sound.SoundManager;
import edu.autonoma.turboclash.navigation.IstartWindowListener;
public class StartWindow {
    public JPanel panel1;
    private JButton btnJugar;
    private SoundManager soundManager = SoundManager.getInstance();
    private final IstartWindowListener listener;

    public StartWindow(IstartWindowListener listener) {
        this.listener = listener;

        SoundManager.getInstance().playBackground(SoundManager.Sound.MENU);

        btnJugar.addActionListener(e -> listener.onPlayPressed());
    }
}