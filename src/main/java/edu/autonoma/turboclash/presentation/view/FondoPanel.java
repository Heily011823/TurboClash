package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Representa y organiza la vista {@code FondoPanel} en la capa de presentacion.
 */
public class FondoPanel extends JPanel {

    private Image imagen;

    /**
     * Crea una nueva instancia de {@code FondoPanel}.
     *
     * @param rutaImagen valor del parametro {@code rutaImagen}
     */
    public FondoPanel(String rutaImagen) {
        URL location = getClass().getResource(rutaImagen);

        if (location != null) {
            imagen = new ImageIcon(location).getImage();
        } else {
            System.out.println("No se encontró la imagen: " + rutaImagen);
            imagen = null;
        }
    }

    @Override
    /**
     * Ejecuta la operacion {@code paintComponent}.
     *
     * @param g contexto grafico utilizado para el renderizado
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
