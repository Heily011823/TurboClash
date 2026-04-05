package edu.autonoma.turboclash.view;

import javax.swing.*;
import edu.autonoma.turboclash.sound.SoundManager;

public class StartWindow {
    public JPanel panel1;
    private JButton btnJugar;
    private SoundManager soundManager = SoundManager.getInstance();

    public StartWindow() {

        SoundManager.getInstance().playBackground(SoundManager.Sound.MENU);

        btnJugar.addActionListener(e -> {

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            frame.dispose();

            new IntroductionWindowFrame();
        });
    }
}