package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.network.*;
import edu.autonoma.turboclash.view.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("TurboClash");
        frame.setContentPane(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        new GameApplication().start();
    }
}
