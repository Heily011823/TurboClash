package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.presentation.view.StartWindowFrame;

import javax.swing.*;

/**
 * Representa la clase `Main` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class Main {

    /**
     * Ejecuta la operacion publica `main`.
     * @param args valor del parametro `args`
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindowFrame::new);
    }
}
