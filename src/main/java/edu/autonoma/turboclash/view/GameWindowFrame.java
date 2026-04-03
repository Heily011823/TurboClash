package edu.autonoma.turboclash.view;

import javax.swing.*;

public class GameWindowFrame extends JFrame {

    public GameWindowFrame() {

        GameWindow view = new GameWindow();

        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Track.png");
        fondo.setLayout(new java.awt.BorderLayout());
        fondo.add(view.panel1);

        setContentPane(fondo);

        setTitle("Juego");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
