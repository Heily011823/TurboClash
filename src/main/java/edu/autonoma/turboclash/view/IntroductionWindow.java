package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindow {

    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;
    private JLabel iconInformacion;

    private final IntroductionWindowListener listener;

    public IntroductionWindow(IntroductionWindowListener listener) {
        this.listener = listener;

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/image/Informacion.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        iconInformacion.setIcon(new ImageIcon(imagenEscalada));

        btnEmpezar.addActionListener(e -> {
            String playerName = txtNombre.getText().trim();
            listener.onContinuePressed(playerName);
        });
    }
}