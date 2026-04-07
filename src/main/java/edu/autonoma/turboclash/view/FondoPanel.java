package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;

public class FondoPanel extends JPanel {

    private Image imagen;

    public FondoPanel(String ruta) {imagen = new ImageIcon(getClass().getResource(ruta)).getImage();}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}