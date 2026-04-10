package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;

import edu.autonoma.turboclash.infrastructure.sound.IAudioService;
import edu.autonoma.turboclash.presentation.navigation.IstartWindowListener;

/**
 * Representa la clase `StartWindow` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class StartWindow {

    /**
     * Expone el atributo publico `panel1` para la colaboracion entre componentes del sistema.
     */
    public JPanel panel1;
    private JButton btnJugar;
    private JButton btnExit;

    private final IstartWindowListener listener;
    private final IAudioService audioService;

    /**
     * Crea una nueva instancia de `StartWindow`.
     * @param listener valor del parametro `listener`
     * @param audioService valor del parametro `audioService`
     */
    public StartWindow(IstartWindowListener listener, IAudioService audioService) {
        this.listener = listener;
        this.audioService = audioService;

        audioService.playMenuMusic();
        ensureComponentsInitialized();

        setupUI();
        setupEvents();
    }

    private void ensureComponentsInitialized() {
        if (panel1 == null) {
            panel1 = new JPanel();
        }
        if (btnJugar == null) {
            btnJugar = new JButton();
        }
        if (btnExit == null) {
            btnExit = new JButton();
        }
    }

    private void setupUI() {
        panel1.setLayout(null);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        btnExit = new JButton("X");
        btnExit.setBounds(screen.width - 80, 20, 50, 50);
        btnExit.setBackground(new Color(180, 20, 20));
        btnExit.setForeground(new Color(255, 255, 255));
        btnExit.setFont(new Font("Arial", Font.BOLD, 18));
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel1.add(btnExit);

        btnJugar = new JButton("PLAY");
        btnJugar.setBounds(
                screen.width / 2 - 120,
                screen.height / 2,
                240,
                80
        );

        btnJugar.setBackground(new Color(200, 0, 0));
        btnJugar.setForeground(new Color(255, 255, 255));
        btnJugar.setFont(new Font("Consolas", Font.BOLD, 22));
        btnJugar.setFocusPainted(false);
        btnJugar.setBorderPainted(false);
        btnJugar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel1.add(btnJugar);
    }

    private void setupEvents() {

        btnExit.addActionListener(e -> System.exit(0));

        btnJugar.addActionListener(e -> listener.onPlayPressed());
    }
}
