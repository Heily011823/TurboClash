package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class FondoPanel extends JPanel {

    private Image imagen;

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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}