package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.presentation.view.StartWindowFrame;

import javax.swing.*;

/**
 * Representa la clase `Main` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
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
