package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Representa y organiza la vista {@code EndGameWindow} en la capa de presentacion.
 */
public class EndGameWindow {
    public JPanel panel1;
    private JLabel PrimerLugar;
    private JLabel SegundoLugar;
    private JLabel TercerLugar;
    private JLabel CuartoLugar;
    private JButton btnFin;
    private JButton btnReiniciar;

    public EndGameWindow() {
        panel1 = new JPanel(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(1000, 700));

        PrimerLugar = crearLabel("", 242, 208, 420, 50);
        SegundoLugar = crearLabel("", 80, 260, 360, 50);
        TercerLugar = crearLabel("", 360, 260, 360, 50);
        CuartoLugar = crearLabel("", 240, 440, 420, 50);

        btnFin = crearBotonImagen("/image/btn_fin.png", 540, 510, 180, 55);
        btnReiniciar = crearBotonImagen("/image/btn_reiniciar.png", 70, 510, 180, 55);

        if (btnFin.getIcon() == null) {
            btnFin.setText("FIN");
            btnFin.setContentAreaFilled(true);
        }

        if (btnReiniciar.getIcon() == null) {
            btnReiniciar.setText("REINICIAR");
            btnReiniciar.setContentAreaFilled(true);
        }

        panel1.add(PrimerLugar);
        panel1.add(SegundoLugar);
        panel1.add(TercerLugar);
        panel1.add(CuartoLugar);
        panel1.add(btnFin);
        panel1.add(btnReiniciar);
    }

    private JLabel crearLabel(String texto, int x, int y, int w, int h) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        label.setForeground(new Color(255, 215, 0));
        label.setFont(new Font("Arial", Font.BOLD, 20));
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

    public void setResultados(List<Player> ranking) {
        PrimerLugar.setText(formatearJugador(ranking, 0, "1"));
        SegundoLugar.setText(formatearJugador(ranking, 1, "2"));
        TercerLugar.setText(formatearJugador(ranking, 2, "3"));
        CuartoLugar.setText(formatearJugador(ranking, 3, "4"));
    }

    private String formatearJugador(List<Player> ranking, int index, String posicion) {
        if (ranking == null || index >= ranking.size() || ranking.get(index) == null) {
            return posicion + ". ---";
        }

        Player player = ranking.get(index);

        String nombre = obtenerNombreJugador(player);
        int puntaje = player.getCurrentPoints();

        return posicion + ". " + nombre + " - " + puntaje + " pts";
    }

    private String obtenerNombreJugador(Player player) {
        if (player == null) {
            return "Jugador";
        }

        if (player.getName() != null && !player.getName().isBlank()) {
            return player.getName();
        }

        if (player.getId() != null && !player.getId().isBlank()) {
            return "Jugador " + player.getId();
        }

        return "Jugador";
    }

    public void addFinListener(ActionListener listener) {
        btnFin.addActionListener(listener);
    }

    public void addReiniciarListener(ActionListener listener) {
        btnReiniciar.addActionListener(listener);
    }
}