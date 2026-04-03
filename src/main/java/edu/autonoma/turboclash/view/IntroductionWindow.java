package edu.autonoma.turboclash.view;
import javax.swing.*;

public class IntroductionWindow {
    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;

    public IntroductionWindow() {

        btnEmpezar.addActionListener(e -> {

            String nombre = txtNombre.getText();
            System.out.println("Jugador: " + nombre);

            // cerrar ventana actual
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            frame.dispose();

            // abrir juego
            new GameWindowFrame();
        });
    }
}