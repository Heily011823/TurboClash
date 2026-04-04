package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;

public class FondoAnimadoPanel extends JPanel {
    private Image imagen;
    private int x1 = 0;
    private int velocidad = 8;
    private Timer timer;

    public FondoAnimadoPanel(String ruta) {

        imagen = new ImageIcon(getClass().getResource(ruta)).getImage();


        timer = new Timer(15, e -> {
            x1 -= velocidad;


            if (x1 <= -getWidth()) {
                x1 = 0;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int ancho = getWidth();
        int alto = getHeight();


        g.drawImage(imagen, x1, 0, ancho, alto, this);


        g.drawImage(imagen, x1 + ancho, 0, ancho, alto, this);
    }
}