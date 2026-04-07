package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.sound.IAudioService;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindow {

    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;
    private JLabel lblNombre;
    private JLabel iconInfo;

    private final IntroductionWindowListener listener;
    private final IAudioService audioService;

    public IntroductionWindow(IntroductionWindowListener listener) {
        this(listener, null);
    }

    public IntroductionWindow(IntroductionWindowListener listener, IAudioService audioService) {
        this.listener = listener;
        this.audioService = audioService;

        if (panel1 == null) {
            panel1 = new JPanel();
        }
        if (btnEmpezar == null) {
            btnEmpezar = new JButton();
            panel1.add(btnEmpezar);
        }
        if (txtNombre == null) {
            txtNombre = new JTextField();
            panel1.add(txtNombre);
        }
        if (lblNombre == null) {
            lblNombre = new JLabel("Nombre:");
            panel1.add(lblNombre);
        }
        if (iconInfo == null) {
            iconInfo = new JLabel();
            panel1.add(iconInfo);
        }

        panel1.setLayout(null);

        iconInfo.setBounds(20, 20, 80, 80);

        try {
            ImageIcon infoIcon = new ImageIcon(getClass().getResource("/image/Informacion.png"));
            Image infoImg = infoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconInfo.setIcon(new ImageIcon(infoImg));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de información: " + e.getMessage());
        }

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

        try {
            ImageIcon playIcon = new ImageIcon(getClass().getResource("/image/Play.png"));
            Image playImg = playIcon.getImage().getScaledInstance(200, 80, Image.SCALE_SMOOTH);
            btnEmpezar.setIcon(new ImageIcon(playImg));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el botón Play: " + e.getMessage());
            btnEmpezar.setText("Empezar");
        }

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

            if (audioService != null) {
                audioService.stopMusic();
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