package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.presentation.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.presentation.navigation.IstartWindowListener;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;

/**
 * Representa y organiza la vista {@code StartWindowFrame} en la capa de presentacion.
 */
public class StartWindowFrame extends JFrame implements IstartWindowListener, IntroductionWindowListener {

    /**
     * Crea una nueva instancia de {@code StartWindowFrame}.
     */
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
    /**
     * Atiende {@code PlayPressed}.
     */
    public void onPlayPressed() {
        dispose();
        new IntroductionWindowFrame();
    }

    @Override
    /**
     * Atiende {@code ContinuePressed}.
     *
     * @param playerName valor del parametro {@code playerName}
     */
    public void onContinuePressed(String playerName) {
        dispose();
    }
}
