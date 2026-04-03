package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;

public class EndGameWindowFrame extends JFrame {

    private EndGameWindow view;

    public EndGameWindowFrame() {
        view = new EndGameWindow();

        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/podium.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);
        setTitle("FIN");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}