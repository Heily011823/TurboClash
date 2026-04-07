package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindow {

    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;
    private JLabel lblNombre;
    private JLabel iconInfo;

    private final IntroductionWindowListener listener;

    public IntroductionWindow(IntroductionWindowListener listener) {
        this.listener = listener;

        panel1.setLayout(null);

        iconInfo.setBounds(20, 20, 80, 80);

        ImageIcon infoIcon = new ImageIcon(getClass().getResource("/image/Informacion.png"));
        Image infoImg = infoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        iconInfo.setIcon(new ImageIcon(infoImg));
        iconInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        iconInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarReglas();
            }
        });

        lblNombre.setBounds(300, 260, 200, 30);
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setForeground(Color.BLACK);

        txtNombre.setBounds(280, 300, 240, 35);
        txtNombre.setHorizontalAlignment(JTextField.CENTER);

        btnEmpezar.setBounds(300, 380, 200, 80);

        ImageIcon playIcon = new ImageIcon(getClass().getResource("/image/Play.png"));
        Image playImg = playIcon.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);
        btnEmpezar.setIcon(new ImageIcon(playImg));

        btnEmpezar.setText("");
        btnEmpezar.setBorderPainted(false);
        btnEmpezar.setContentAreaFilled(false);
        btnEmpezar.setFocusPainted(false);
        btnEmpezar.setOpaque(false);
        btnEmpezar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnEmpezar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingresa tu nombre");
                return;
            }

            listener.onContinuePressed(nombre);
        });
    }

    private void mostrarReglas() {
        String reglas = """
                REGLAS DEL JUEGO

                """;

        JOptionPane.showMessageDialog(
                null,
                reglas,
                "Reglas del juego",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}