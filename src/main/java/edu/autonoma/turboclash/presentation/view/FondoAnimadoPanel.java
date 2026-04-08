package edu.autonoma.turboclash.presentation.view;



import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class FondoAnimadoPanel extends JPanel {

    private Image carretera;
    private Image meta;

    private int x1 = 0;
    private final int velocidad = 8;

    private Timer timerMovimiento;
    private Timer timerTiempo;

    private int segundosRestantes = 180;
    private boolean tiempoTerminado = false;
    private boolean mostrarMeta = false;
    private boolean juegoTerminado = false;

    private int metaX;

    public FondoAnimadoPanel(String ruta) {
        URL rutaCarretera = getClass().getResource(ruta);
        if (rutaCarretera != null) {
            carretera = new ImageIcon(rutaCarretera).getImage();
        } else {
            System.out.println("No se encontró la imagen: " + ruta);
        }

        URL rutaMeta = getClass().getResource("/image/Finish.png");
        if (rutaMeta != null) {
            meta = new ImageIcon(rutaMeta).getImage();
        } else {
            System.out.println("No se encontró la imagen: /image/Finish.png");
        }

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

    public void terminarJuego(List<Player> ranking) {
        if (juegoTerminado) return;

        juegoTerminado = true;

        timerMovimiento.stop();
        timerTiempo.stop();

        String primero = ranking.size() > 0 ? ranking.get(0).getName() : "";
        String segundo = ranking.size() > 1 ? ranking.get(1).getName() : "";
        String tercero = ranking.size() > 2 ? ranking.get(2).getName() : "";
        String cuarto = ranking.size() > 3 ? ranking.get(3).getName() : "";

        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null) {
            ventana.dispose();
        }

        new EndGameWindowFrame(primero, segundo, tercero, cuarto);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int ancho = getWidth();
        int alto = getHeight();

        if (carretera != null) {
            g.drawImage(carretera, x1, 0, ancho, alto, this);
            g.drawImage(carretera, x1 + ancho, 0, ancho, alto, this);
        }

        if (mostrarMeta && meta != null) {
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