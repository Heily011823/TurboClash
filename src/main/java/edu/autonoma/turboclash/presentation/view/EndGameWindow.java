package edu.autonoma.turboclash.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Representa la clase `EndGameWindow` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class EndGameWindow {
    /**
     * Expone el atributo publico `panel1` para la colaboracion entre componentes del sistema.
     */
    public JPanel panel1;

    private JLabel tituloLabel;
    private JLabel ganadorLabel;
    private JLabel tiempoTotalLabel;

    private JLabel PrimerLugar;
    private JLabel SegundoLugar;
    private JLabel TercerLugar;
    private JLabel CuartoLugar;

    private JButton btnFin;
    private JButton btnReiniciar;

    /**
     * Crea una nueva instancia de `EndGameWindow`.
     */
    public EndGameWindow() {
        panel1 = new JPanel(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(1000, 700));

        tituloLabel = crearLabel("RESULTADOS FINALES", 200, 35, 400, 40, 28, new Color(255, 255, 255));
        ganadorLabel = crearLabel("", 160, 90, 500, 35, 22, new Color(255, 215, 0));
        tiempoTotalLabel = crearLabel("", 220, 125, 350, 30, 18, new Color(230, 230, 230));

        PrimerLugar = crearLabel("", 170, 190, 460, 40, 20, new Color(255, 215, 0));
        SegundoLugar = crearLabel("", 120, 245, 320, 40, 18, new Color(230, 230, 230));
        TercerLugar = crearLabel("", 360, 245, 320, 40, 18, new Color(205, 127, 50));
        CuartoLugar = crearLabel("", 170, 405, 460, 40, 18, new Color(220, 220, 220));

        btnFin = crearBotonImagen("/image/btn_fin.png", 520, 500, 180, 55);
        btnReiniciar = crearBotonImagen("/image/btn_reiniciar.png", 80, 500, 180, 55);

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
        panel1.add(PrimerLugar);
        panel1.add(SegundoLugar);
        panel1.add(TercerLugar);
        panel1.add(CuartoLugar);
        panel1.add(btnFin);
        panel1.add(btnReiniciar);
    }

    private JLabel crearLabel(String texto, int x, int y, int w, int h, int fontSize, Color color) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setBounds(x, y, w, h);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
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
            System.out.println("No se encontrÃ³ la imagen del botÃ³n: " + ruta);
        }

        return boton;
    }

    /**
     * Actualiza el valor asociado a `setResultados`.
     * @param ganador valor del parametro `ganador`
     * @param tiempoTotal valor del parametro `tiempoTotal`
     * @param primero valor del parametro `primero`
     * @param segundo valor del parametro `segundo`
     * @param tercero valor del parametro `tercero`
     * @param cuarto valor del parametro `cuarto`
     */
    public void setResultados(String ganador,
                              String tiempoTotal,
                              String primero,
                              String segundo,
                              String tercero,
                              String cuarto) {

        ganadorLabel.setText("Ganador: " + ganador);
        tiempoTotalLabel.setText("Tiempo total de partida: " + tiempoTotal);

        PrimerLugar.setText(primero);
        SegundoLugar.setText(segundo);
        TercerLugar.setText(tercero);
        CuartoLugar.setText(cuarto);
    }

    /**
     * Agrega el elemento necesario para add fin listener.
     * @param listener valor del parametro `listener`
     */
    public void addFinListener(ActionListener listener) {
        btnFin.addActionListener(listener);
    }

    /**
     * Agrega el elemento necesario para add reiniciar listener.
     * @param listener valor del parametro `listener`
     */
    public void addReiniciarListener(ActionListener listener) {
        btnReiniciar.addActionListener(listener);
    }
}
