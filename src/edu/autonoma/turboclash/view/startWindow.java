/*
 * Created by JFormDesigner on Thu Apr 02 09:39:45 GMT-05:00 2026
 */

package edu.autonoma.turboclash.view;

import javax.swing.*;

public class startWindow extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblImagen;
    private JButton btnRun;

    public startWindow() {
        setContentPane(panelPrincipal);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panelPrincipal.setLayout(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new startWindow().setVisible(true));
    }
}