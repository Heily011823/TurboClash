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


        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/image/Informacion.png"));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            iconInformacion.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }


        btnEmpezar.addActionListener(e -> {
            String playerName = txtNombre.getText().trim();


            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(panel1, "¡Debes ingresar un nombre!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }


            cerrarVentana();


            listener.onContinuePressed(playerName);
        });
    }

    private void cerrarVentana() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (frame != null) {
            frame.dispose();
        }
    }
}