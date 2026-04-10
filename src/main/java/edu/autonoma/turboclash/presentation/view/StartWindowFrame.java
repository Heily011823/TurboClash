package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.presentation.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.presentation.navigation.IstartWindowListener;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;

/**
 * Representa la clase `StartWindowFrame` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class StartWindowFrame extends JFrame implements IstartWindowListener, IntroductionWindowListener {

    /**
     * Crea una nueva instancia de `StartWindowFrame`.
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
     * Ejecuta la operacion publica `onPlayPressed`.
     */
    public void onPlayPressed() {
        dispose();
        new IntroductionWindowFrame();
    }

    @Override
    /**
     * Ejecuta la operacion publica `onContinuePressed`.
     * @param playerName valor del parametro `playerName`
     */
    public void onContinuePressed(String playerName) {
        dispose();
    }
}
