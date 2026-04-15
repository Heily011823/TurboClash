package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista de resultados finales del juego.
 *
 * <p>Esta clase construye y organiza los componentes visuales
 * de la pantalla final: título, ganador, tiempo total, ranking
 * y botones de acción.</p>
 *
 * <p>Aquí se controlan:</p>
 * <ul>
 *   <li>Los textos que aparecen en pantalla.</li>
 *   <li>La posición de cada label.</li>
 *   <li>El tamaño y color de la fuente.</li>
 * </ul>
 *
 * @author Valerie Moreno Castaño
 * @version 1.1
 * @since 2025-04-09
 */
public class EndGameWindow {

    public JPanel panel1;

    private JLabel tituloLabel;
    private JLabel ganadorLabel;
    private JLabel tiempoTotalLabel;

    private JLabel primerLugarLabel;
    private JLabel segundoLugarLabel;
    private JLabel tercerLugarLabel;
    private JLabel cuartoLugarLabel;

    private JButton btnFin;
    private JButton btnReiniciar;

    public EndGameWindow() {
        panel1 = new JPanel(null);
        panel1.setOpaque(false);

        // Mejor alinear esto con el tamaño real del frame
        panel1.setPreferredSize(new Dimension(800, 600));

        tituloLabel = crearLabel("RESULTADOS FINALES", 170, 30, 460, 45, 30, Color.WHITE);
        ganadorLabel = crearLabel("", 150, 85, 500, 35, 24, Color.YELLOW);
        tiempoTotalLabel = crearLabel("", 130, 120, 540, 30, 20, Color.WHITE);

        // Labels del ranking con más ancho y fuente más visible
        primerLugarLabel = crearLabel("", 150, 185, 500, 40, 22, Color.YELLOW);
        segundoLugarLabel = crearLabel("", 60, 245, 300, 40, 20, Color.WHITE);
        tercerLugarLabel = crearLabel("", 430, 245, 300, 40, 20, new Color(255, 200, 120));
        cuartoLugarLabel = crearLabel("", 150, 395, 500, 40, 20, Color.WHITE);

        btnFin = crearBotonImagen("/image/btn_fin.png", 500, 500, 180, 55);
        btnReiniciar = crearBotonImagen("/image/btn_reiniciar.png", 100, 500, 180, 55);

        if (btnFin.getIcon() == null) {
            btnFin.setText("FIN");
            btnFin.setContentAreaFilled(true);
        }

        if (btnReiniciar.getIcon() == null) {
            btnReiniciar.setText("REINICIAR");
            btnReiniciar.setContentAreaFilled(true);
        }

        panel1.add(tituloLabel);
        panel1.add(ganadorLabel);
        panel1.add(tiempoTotalLabel);
        panel1.add(primerLugarLabel);
        panel1.add(segundoLugarLabel);
        panel1.add(tercerLugarLabel);
        panel1.add(cuartoLugarLabel);
        panel1.add(btnFin);
        panel1.add(btnReiniciar);
    }

    private JLabel crearLabel(String texto, int x, int y, int w, int h, int fontSize, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        label.setForeground(color);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        return label;
    }

    private JButton crearBotonImagen(String ruta, int x, int y, int w, int h) {
        JButton boton = new JButton();
        boton.setBounds(x, y, w, h);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setOpaque(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(ruta));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            boton.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception e) {
            System.out.println("No se encontró la imagen del botón: " + ruta);
        }

        return boton;
    }

    /**
     * Muestra los resultados finales en pantalla.
     *
     * @param ganador nombre del ganador
     * @param tiempoTotal tiempo total de la partida
     * @param primero texto del primer lugar
     * @param segundo texto del segundo lugar
     * @param tercero texto del tercer lugar
     * @param cuarto texto del cuarto lugar
     */
    public void setResultados(String ganador,
                              String tiempoTotal,
                              String primero,
                              String segundo,
                              String tercero,
                              String cuarto) {

        ganadorLabel.setText("Ganador: " + ganador);
        tiempoTotalLabel.setText("Tiempo total: " + tiempoTotal);

        primerLugarLabel.setText(primero);
        segundoLugarLabel.setText(segundo);
        tercerLugarLabel.setText(tercero);
        cuartoLugarLabel.setText(cuarto);
    }

    public void addFinListener(ActionListener listener) {
        btnFin.addActionListener(listener);
    }

    public void addReiniciarListener(ActionListener listener) {
        btnReiniciar.addActionListener(listener);
    }
}