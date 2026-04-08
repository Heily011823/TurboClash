package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;

public class EndGameWindowFrame extends JFrame {

    private EndGameWindow view;

    public EndGameWindowFrame(String primero, String segundo, String tercero, String cuarto) {
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

        view.setResultados(primero, segundo, tercero, cuarto);

        view.addFinListener(e -> System.exit(0));

        view.addReiniciarListener(e -> {
            dispose();
            new StartWindowFrame(); // cambia esto si tu ventana inicial tiene otro nombre
        });

        setVisible(true);
    }
}