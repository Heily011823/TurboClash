package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

/**
 * Representa la clase `FondoAnimadoPanel` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class FondoAnimadoPanel extends JPanel {

    private static final int DURACION_PARTIDA_SEGUNDOS = 180;

    private Image carretera;
    private Image meta;

    private int x1 = 0;
    private final int velocidad = 8;

    private Timer timerMovimiento;
    private Timer timerTiempo;

    private int segundosRestantes = DURACION_PARTIDA_SEGUNDOS;
    private boolean tiempoTerminado = false;
    private boolean mostrarMeta = false;
    private boolean juegoTerminado = false;
    private boolean juegoIniciado = false;

    private int metaX;

    /**
     * Crea una nueva instancia de `FondoAnimadoPanel`.
     * @param ruta valor del parametro `ruta`
     */
    public FondoAnimadoPanel(String ruta) {
        URL rutaCarretera = getClass().getResource(ruta);
        if (rutaCarretera != null) {
            carretera = new ImageIcon(rutaCarretera).getImage();
        } else {
            System.out.println("No se encontrÃ³ la imagen: " + ruta);
        }

        URL rutaMeta = getClass().getResource("/image/Finish.png");
        if (rutaMeta != null) {
            meta = new ImageIcon(rutaMeta).getImage();
        } else {
            System.out.println("No se encontrÃ³ la imagen: /image/Finish.png");
        }

        metaX = GameViewport.WIDTH;
        crearTimers();
    }

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
     * Inicia el flujo asociado a start game.
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

    /**
     * Ejecuta la operacion publica `pauseGame`.
     */
    public void pauseGame() {
        if (timerMovimiento != null) {
            timerMovimiento.stop();
        }
        if (timerTiempo != null) {
            timerTiempo.stop();
        }
    }

    /**
     * Ejecuta la operacion publica `resumeGame`.
     */
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

    /**
     * Ejecuta la operacion publica `resetGame`.
     */
    public void resetGame() {
        pauseGame();

        x1 = 0;
        segundosRestantes = DURACION_PARTIDA_SEGUNDOS;
        tiempoTerminado = false;
        mostrarMeta = false;
        juegoTerminado = false;
        juegoIniciado = false;
        metaX = GameViewport.WIDTH;

        repaint();
    }

    /**
     * Obtiene el valor asociado a `getMetaX`.
     * @return resultado de la operacion documentada
     */
    public int getMetaX() {
        return metaX;
    }

    /**
     * Indica la condicion evaluada por `isMetaVisible`.
     * @return resultado de la operacion documentada
     */
    public boolean isMetaVisible() {
        return mostrarMeta;
    }

    /**
     * Indica la condicion evaluada por `isJuegoTerminado`.
     * @return resultado de la operacion documentada
     */
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    /**
     * Indica la condicion evaluada por `isJuegoIniciado`.
     * @return resultado de la operacion documentada
     */
    public boolean isJuegoIniciado() {
        return juegoIniciado;
    }

    /**
     * Obtiene el valor asociado a `getSegundosRestantes`.
     * @return resultado de la operacion documentada
     */
    public int getSegundosRestantes() {
        return segundosRestantes;
    }

    /**
     * Ejecuta la operacion publica `terminarJuego`.
     * @param ranking valor del parametro `ranking`
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

        String ganador = ranking != null && !ranking.isEmpty()
                ? safeName(ranking.get(0))
                : "Sin ganador";

        String tiempoTotal = formatear(DURACION_PARTIDA_SEGUNDOS - segundosRestantes);

        String primero = formatRankingLine(1, ranking, 0);
        String segundo = formatRankingLine(2, ranking, 1);
        String tercero = formatRankingLine(3, ranking, 2);
        String cuarto = formatRankingLine(4, ranking, 3);

        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null) {
            ventana.dispose();
        }

        new EndGameWindowFrame(ganador, tiempoTotal, primero, segundo, tercero, cuarto);
    }

    private String formatRankingLine(int posicion, List<Player> ranking, int index) {
        if (ranking == null || ranking.size() <= index || ranking.get(index) == null) {
            return posicion + ". ---";
        }

        Player player = ranking.get(index);
        String estado = getEstado(player);

        return posicion + ". "
                + safeName(player)
                + " - "
                + player.getCurrentPoints()
                + " pts"
                + " - "
                + estado;
    }

    private String getEstado(Player player) {
        if (player == null) {
            return "Sin estado";
        }

        if (player.isFinishReached()) {
            return "LlegÃ³ a la meta";
        }

        if (player.isAlive()) {
            return "Sigue activo";
        }

        return "Eliminado";
    }

    private String safeName(Player player) {
        if (player == null || player.getName() == null || player.getName().isBlank()) {
            return "Jugador";
        }
        return player.getName();
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
