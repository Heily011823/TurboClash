package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.presentation.view.StartWindowFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindowFrame::new);
    }
}