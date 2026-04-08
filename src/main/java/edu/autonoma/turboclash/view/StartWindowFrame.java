package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.navigation.IstartWindowListener;
import edu.autonoma.turboclash.sound.SoundManager;

import javax.swing.*;
import java.awt.*;

public class StartWindowFrame extends JFrame implements IstartWindowListener, IntroductionWindowListener {

    public StartWindowFrame() {

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        StartWindow view = new StartWindow(this, SoundManager.getInstance());
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Game_Cover.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void onPlayPressed() {
        dispose();
        new IntroductionWindowFrame();
    }

    @Override
    public void onContinuePressed(String playerName) {
        dispose();
    }
}