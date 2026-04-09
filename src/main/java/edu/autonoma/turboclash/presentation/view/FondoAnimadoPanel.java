package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * Representa y organiza la vista {@code FondoAnimadoPanel} en la capa de presentacion.
 */
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
    private boolean juegoIniciado = false;

    private int metaX;

    /**
     * Crea una nueva instancia de {@code FondoAnimadoPanel}.
     *
     * @param ruta valor del parametro {@code ruta}
     */
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

        crearTimers();
    }

    /**
     * Crea los timers pero NO los inicia.
     */
    private void crearTimers() {
        timerMovimiento = new Timer(15, e -> {
            if (juegoTerminado || !juegoIniciado) return;

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

        timerTiempo = new Timer(1000, e -> {
            if (juegoTerminado || !juegoIniciado) return;

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
    }

    /**
     * Inicia el movimiento y el tiempo solo cuando empieza la partida.
     */
    public void startGame() {
        if (juegoTerminado || juegoIniciado) {
            return;
        }

        juegoIniciado = true;

        if (timerMovimiento != null && !timerMovimiento.isRunning()) {
            timerMovimiento.start();
        }

        if (timerTiempo != null && !timerTiempo.isRunning()) {
            timerTiempo.start();
        }

        repaint();
    }

    public void pauseGame() {
        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }
        if (timerTiempo != null) {
            timerTiempo.stop();
        }
    }

    public void resumeGame() {
        if (juegoTerminado || !juegoIniciado) {
            return;
        }

        if (timerMovimiento != null && !timerMovimiento.isRunning()) {
            timerMovimiento.start();
        }
        if (timerTiempo != null && !timerTiempo.isRunning()) {
            timerTiempo.start();
        }
    }

    public void resetGame() {
        pauseGame();

        x1 = 0;
        segundosRestantes = 180;
        tiempoTerminado = false;
        mostrarMeta = false;
        juegoTerminado = false;
        juegoIniciado = false;
        metaX = GameViewport.WIDTH;

        repaint();
    }

    /**
     * Obtiene el valor de {@code MetaX}.
     *
     * @return valor de {@code MetaX}
     */
    public int getMetaX() {
        return metaX;
    }

    /**
     * Indica si {@code MetaVisible}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isMetaVisible() {
        return mostrarMeta;
    }

    /**
     * Indica si {@code JuegoTerminado}.
     *
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean isJuegoIniciado() {
        return juegoIniciado;
    }

    public int getSegundosRestantes() {
        return segundosRestantes;
    }

    /**
     * Ejecuta la operacion {@code terminarJuego}.
     *
     * @param ranking valor del parametro {@code ranking}
     */
    public void terminarJuego(List<Player> ranking) {
        if (juegoTerminado) return;

        juegoTerminado = true;
        juegoIniciado = false;

        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }
        if (timerTiempo != null) {
            timerTiempo.stop();
        }

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

    /**
     * Ejecuta la operacion {@code formatear}.
     *
     * @param s valor del parametro {@code s}
     * @return resultado de la operacion {@code formatear}
     */
    private String formatear(int s) {
        int min = s / 60;
        int seg = s % 60;
        return String.format("%02d:%02d", min, seg);
    }
}