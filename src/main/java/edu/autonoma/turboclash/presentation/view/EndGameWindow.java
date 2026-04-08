package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        panel1.setPreferredSize(new Dimension(800, 600));

        PrimerLugar = crearLabel("", 330, 300, 150, 40);
        SegundoLugar = crearLabel("", 180, 380, 150, 40);
        TercerLugar = crearLabel("", 500, 380, 150, 40);
        CuartoLugar = crearLabel("", 330, 470, 150, 40);

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

    public void setResultados(String primero, String segundo, String tercero, String cuarto) {
        PrimerLugar.setText(primero);
        SegundoLugar.setText(segundo);
        TercerLugar.setText(tercero);
        CuartoLugar.setText(cuarto);
    }

    public void addFinListener(ActionListener listener) {
        btnFin.addActionListener(listener);
    }

    public void addReiniciarListener(ActionListener listener) {
        btnReiniciar.addActionListener(listener);
    }
}