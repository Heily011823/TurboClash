package edu.autonoma.turboclash.view;
import javax.swing.*;

public class IntroductionWindowFrame extends JFrame {

    public IntroductionWindowFrame() {

        IntroductionWindow view = new IntroductionWindow();
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Start.png");
        fondo.setLayout(new java.awt.BorderLayout());
        fondo.add(view.panel1);

        setContentPane(fondo);
        setTitle("Introducción");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
