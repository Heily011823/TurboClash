package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.presentation.view.StartWindowFrame;

import javax.swing.*;

/**
 * Define el punto de entrada principal de la aplicacion.
 */
public class Main {

    /**
     * Ejecuta la operacion {@code main}.
     *
     * @param args valor del parametro {@code args}
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindowFrame::new);
    }
}
