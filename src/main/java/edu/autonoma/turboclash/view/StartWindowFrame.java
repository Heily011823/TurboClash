package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;

public class StartWindowFrame extends JFrame {

    public StartWindowFrame() {

        StartWindow view = new StartWindow();
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Game_Cover.png");

        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1);

        setContentPane(fondo);

        setTitle("Menú");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}