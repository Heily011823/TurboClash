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
 * <p>Incluye mejoras visuales como sombra en texto para mayor legibilidad.</p>
 *
 * @author Valerie Moreno Castaño
 * @version 1.2
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

    /**
     * Constructor de la vista final.
     */
    public EndGameWindow() {

        panel1 = new JPanel(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(800, 600));

        // Labels con sombra (ShadowLabel)
        tituloLabel = crearLabel("RESULTADOS FINALES", 170, 30, 460, 45, 30, Color.WHITE);
        ganadorLabel = crearLabel("", 120, 85, 560, 35, 24, Color.YELLOW);
        tiempoTotalLabel = crearLabel("", 120, 120, 560, 30, 20, Color.WHITE);

        primerLugarLabel = crearLabel("", 140, 185, 520, 40, 22, Color.YELLOW);
        segundoLugarLabel = crearLabel("", 40, 245, 320, 40, 20, Color.WHITE);
        tercerLugarLabel = crearLabel("", 440, 245, 320, 40, 20, new Color(255, 200, 120));
        cuartoLugarLabel = crearLabel("", 140, 395, 520, 40, 20, Color.WHITE);

        // Botones
        btnFin = crearBotonImagen("/image/btn_fin.png", 500, 500, 180, 55);
        btnReiniciar = crearBotonImagen("/image/btn_reiniciar.png", 100, 500, 180, 55);

        // Fallback si no hay imágenes
        if (btnFin.getIcon() == null) {
            btnFin.setText("FIN");
            btnFin.setContentAreaFilled(true);
        }

        if (btnReiniciar.getIcon() == null) {
            btnReiniciar.setText("REINICIAR");
            btnReiniciar.setContentAreaFilled(true);
        }

        // Agregar componentes
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

    /**
     * Crea un label con sombra (ShadowLabel).
     *
     * @param texto texto inicial
     * @param x posición horizontal
     * @param y posición vertical
     * @param w ancho
     * @param h alto
     * @param fontSize tamaño de fuente
     * @param color color del texto
     * @return label configurado
     */
    private JLabel crearLabel(String texto, int x, int y, int w, int h, int fontSize, Color color) {
        ShadowLabel label = new ShadowLabel(texto);
        label.setBounds(x, y, w, h);
        label.setForeground(color);
        label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Crea un botón con imagen de fondo.
     */
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
     * Muestra los resultados finales.
     *
     * @param ganador ganador del juego
     * @param tiempoTotal tiempo total de partida
     * @param primero primer lugar
     * @param segundo segundo lugar
     * @param tercero tercer lugar
     * @param cuarto cuarto lugar (puede ser null)
     */
    public void setResultados(String ganador,
                              String tiempoTotal,
                              String primero,
                              String segundo,
                              String tercero,
                              String cuarto) {

        ganadorLabel.setText("Ganador: " + (ganador != null ? ganador : ""));
        tiempoTotalLabel.setText("Tiempo total: " + (tiempoTotal != null ? tiempoTotal : ""));

        primerLugarLabel.setText(primero != null ? primero : "");
        segundoLugarLabel.setText(segundo != null ? segundo : "");
        tercerLugarLabel.setText(tercero != null ? tercero : "");

        // Ocultar cuarto lugar si no existe
        if (cuarto == null || cuarto.isBlank()) {
            cuartoLugarLabel.setText("");
            cuartoLugarLabel.setVisible(false);
        } else {
            cuartoLugarLabel.setVisible(true);
            cuartoLugarLabel.setText(cuarto);
        }
    }

    /**
     * Listener botón finalizar.
     */
    public void addFinListener(ActionListener listener) {
        btnFin.addActionListener(listener);
    }

    /**
     * Listener botón reiniciar.
     */
    public void addReiniciarListener(ActionListener listener) {
        btnReiniciar.addActionListener(listener);
    }
}