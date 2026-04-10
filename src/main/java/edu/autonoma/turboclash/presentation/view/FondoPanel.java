package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Representa la clase `FondoPanel` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class FondoPanel extends JPanel {

    private Image imagen;

    /**
     * Crea una nueva instancia de `FondoPanel`.
     * @param rutaImagen valor del parametro `rutaImagen`
     */
    public FondoPanel(String rutaImagen) {
        URL location = getClass().getResource(rutaImagen);

        if (location != null) {
            imagen = new ImageIcon(location).getImage();
        } else {
            System.out.println("No se encontrÃ³ la imagen: " + rutaImagen);
            imagen = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
