package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.main.GameApplication;
import edu.autonoma.turboclash.validation.PlayerNameValidator;
import edu.autonoma.turboclash.exception.InvalidNameException;
import edu.autonoma.turboclash.exception.DuplicateDataException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class IntroductionWindow {

    public JPanel panel1;
    private JButton btnEmpezar;
    private JTextField txtNombre;
    private JTextField txtNombre2;
    private JTextField txtNombre3;
    private JTextField txtNombre4;
    private JLabel iconInformacion;

    public IntroductionWindow() {

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/image/Start.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        iconInformacion.setIcon(new ImageIcon(imagenEscalada));

        btnEmpezar.addActionListener(e -> iniciarJuego());
    }


    private void iniciarJuego() {
        try {
            List<String> jugadores = obtenerJugadores();

            validarJugadores(jugadores);

            cerrarVentana();

            new Thread(() -> new GameApplication().start()).start();

        } catch (InvalidNameException | DuplicateDataException ex) {
            mostrarError(ex.getMessage());
        }
    }


    private List<String> obtenerJugadores() {
        List<String> jugadores = new ArrayList<>();

        jugadores.add(txtNombre.getText().trim());
        jugadores.add(txtNombre2.getText().trim());
        jugadores.add(txtNombre3.getText().trim());
        jugadores.add(txtNombre4.getText().trim());

        return jugadores;
    }


    private void validarJugadores(List<String> jugadores)
            throws InvalidNameException, DuplicateDataException {

        for (int i = 0; i < jugadores.size(); i++) {
            String actual = jugadores.get(i);

            // Validación individual
            PlayerNameValidator.validate(actual, null);

            // Validación de duplicados
            for (int j = i + 1; j < jugadores.size(); j++) {
                PlayerNameValidator.validate(actual, jugadores.get(j));
            }
        }
    }

    private void cerrarVentana() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
        if (frame != null) {
            frame.dispose();
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}