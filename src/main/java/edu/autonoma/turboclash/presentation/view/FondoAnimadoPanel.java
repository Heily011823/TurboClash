package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;

public class FondoAnimadoPanel extends JPanel {

    private Image carretera;
    private Image meta;

    private int x1 = 0;
    private int velocidad = 8;

    private Timer timerMovimiento;
    private Timer timerTiempo;

    private int segundosRestantes = 180; // 3 minutos
    private boolean tiempoTerminado = false;
    private boolean mostrarMeta = false;
    private boolean juegoTerminado = false;

    private int metaX;

    public FondoAnimadoPanel(String ruta) {
        carretera = new ImageIcon(getClass().getResource(ruta)).getImage();
        meta = new ImageIcon(getClass().getResource("/image/Finish.png")).getImage();

        metaX = GameViewport.WIDTH;

        iniciarMovimiento();
        iniciarTiempo();
    }

    private void iniciarMovimiento() {
        timerMovimiento = new Timer(15, e -> {
            if (juegoTerminado) return;

            x1 -= velocidad;

            if (x1 <= -getWidth()) {
                x1 = 0;
            }

            // La meta solo aparece cuando se acaban los 3 minutos
            if (tiempoTerminado && mostrarMeta) {
                if (metaX > getWidth() - 200) {
                    metaX -= velocidad;
                }
            }

            repaint();
        });

        timerMovimiento.start();
    }

    private void iniciarTiempo() {
        timerTiempo = new Timer(1000, e -> {
            if (juegoTerminado) return;

            if (!tiempoTerminado) {
                segundosRestantes--;

                if (segundosRestantes <= 0) {
                    segundosRestantes = 0;
                    tiempoTerminado = true;
                    mostrarMeta = true;
                    metaX = getWidth();
                }
            }

            repaint();
        });

        timerTiempo.start();
    }

    public int getMetaX() {
        return metaX;
    }

    public boolean isMetaVisible() {
        return mostrarMeta;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void terminarJuego() {
        if (juegoTerminado) return;

        juegoTerminado = true;

        timerMovimiento.stop();
        timerTiempo.stop();

        JOptionPane.showMessageDialog(this, "¡Llegaste a la meta!");

        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null) {
            ventana.dispose();
        }

        new EndGameWindowFrame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int ancho = getWidth();
        int alto = getHeight();

        g.drawImage(carretera, x1, 0, ancho, alto, this);
        g.drawImage(carretera, x1 + ancho, 0, ancho, alto, this);

        if (mostrarMeta) {
            g.drawImage(meta, metaX, 0, 200, alto, this);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Tiempo: " + formatear(segundosRestantes), 20, 30);
    }

    private String formatear(int s) {
        int min = s / 60;
        int seg = s % 60;
        return String.format("%02d:%02d", min, seg);
    }
}
