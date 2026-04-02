package edu.autonoma.turboclash.view;

import javax.swing.*;

public class startWindow extends JFrame {
    private JPanel panelPrincipal;
    private JLabel ImagenFondo;
    private JButton button1;
    private JButton btnRun;

    public startWindow() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/edu/autonoma/turboclash/image/Game_Cover.png")
        );
        ImagenFondo.setIcon(icon);
        panelPrincipal.setLayout(null);  // mejor usa el layout que genera el diseñador
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("startWindow");
        frame.setContentPane(new startWindow().panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}