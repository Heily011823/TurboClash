package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IstartWindowListener;

import javax.swing.*;
import java.awt.*;

public class StartWindowFrame extends JFrame implements IstartWindowListener {

    public StartWindowFrame() {

        StartWindow view = new StartWindow(this);
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
    @Override
    public void onPlayPressed() {
        dispose();
        new IntroductionWindowFrame();
    }
}