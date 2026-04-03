package edu.autonoma.turboclash.view;
import javax.swing.*;
import java.awt.*;
import edu.autonoma.turboclash.view.GameWindowFrame;

public class IntroductionWindow {
    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;
    private JTextField txtNombre3;
    private JTextField txtNombre2;
    private JTextField txtNombre4;
    private JLabel iconInformacion;

    public IntroductionWindow() {
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/image/Start.png"));

        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);

        ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

        iconInformacion.setIcon(iconoFinal);
        btnEmpezar.addActionListener(e -> {

            String nombre1 = txtNombre.getText().trim();
            String nombre2 = txtNombre2.getText().trim();
            String nombre3 = txtNombre3.getText().trim();
            String nombre4 = txtNombre4.getText().trim();

            // cerrar ventana actual
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            frame.dispose();

            // abrir juego
            new GameWindowFrame();
        });
    }
}