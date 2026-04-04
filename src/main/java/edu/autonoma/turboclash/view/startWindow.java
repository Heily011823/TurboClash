package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.view.IntroductionWindowFrame;

import javax.swing.*;

public class StartWindow {
    public JPanel panel1;
    private JButton btnJugar;

    public StartWindow() {

        btnJugar.addActionListener(e -> {

            // cerrar ventana actual
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            frame.dispose();

            // abrir la siguiente
            new EndGameWindowFrame();
        });
    }


}