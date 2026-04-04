package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWindow {
    public JPanel panel1;

    private JLabel Vidas;
    private JLabel Puntaje;


    private List<JLabel> listaObstaculos = new ArrayList<>();
    private List<JLabel> listaMonedas = new ArrayList<>();

    private Random random = new Random();
    private int velocidadJuego = 8;
    private Timer motorJuego;

    private String[] imagenesObstaculos = {
            "/image/Barrier.png",
            "/image/Cone.png",
            "/image/Oil_Spill.png"
    };

    public GameWindow() {

        panel1.setLayout(null);
        panel1.setOpaque(false);


        Vidas.setBounds(20, 20, 200, 50);
        Puntaje.setBounds(550, 20, 200, 50);


        Puntaje.setForeground(Color.WHITE);
        Puntaje.setFont(new Font("Arial", Font.BOLD, 22));


        colocarCarros();
        generarObstaculos(3);
        generarMonedas(2);


        motorJuego = new Timer(20, e -> {
            actualizarPosiciones();
        });
        motorJuego.start();
    }

    private void colocarCarros() {

        panel1.add(crearLabelRecurso("/image/Car_Blue.png", 50, 90, 100, 50));
        panel1.add(crearLabelRecurso("/image/Car_Brown.png", 50, 180, 100, 50));
        panel1.add(crearLabelRecurso("/image/Car_Red.png", 50, 290, 100, 50));
        panel1.add(crearLabelRecurso("/image/Car_Yellow.png", 50, 450, 100, 50));
    }

    private void generarObstaculos(int cantidad) {
        int[] carrilesY = {90, 180, 290, 450};
        for (int i = 0; i < cantidad; i++) {
            String ruta = imagenesObstaculos[random.nextInt(imagenesObstaculos.length)];

            JLabel obs = crearLabelRecurso(ruta, 800 + (i * 400), carrilesY[random.nextInt(4)], 60, 60);
            panel1.add(obs);
            listaObstaculos.add(obs);
        }
    }

    private void generarMonedas(int cantidad) {
        int[] carrilesY = {90, 180, 290, 450};
        for (int i = 0; i < cantidad; i++) {

            JLabel moneda = crearLabelRecurso("/image/Coin.png", 1200 + (i * 500), carrilesY[random.nextInt(4)], 40, 40);
            panel1.add(moneda);
            listaMonedas.add(moneda);
        }
    }

    private void actualizarPosiciones() {

        moverYReciclar(listaObstaculos, 60, true);
        moverYReciclar(listaMonedas, 40, false);


        panel1.repaint();
    }

    private void moverYReciclar(List<JLabel> lista, int tamaño, boolean esObstaculo) {
        int[] carrilesY = {90, 180, 290, 450};

        for (JLabel item : lista) {
            int nuevaX = item.getX() - velocidadJuego;


            if (nuevaX < -tamaño) {

                nuevaX = 800 + random.nextInt(800);
                int nuevaY = carrilesY[random.nextInt(4)];


                if (esObstaculo) {
                    String ruta = imagenesObstaculos[random.nextInt(imagenesObstaculos.length)];
                    item.setIcon(new ImageIcon(getClass().getResource(ruta)));
                }

                item.setLocation(nuevaX, nuevaY);
            } else {
                item.setLocation(nuevaX, item.getY());
            }
        }
    }


    private JLabel crearLabelRecurso(String ruta, int x, int y, int w, int h) {
        JLabel label = new JLabel(new ImageIcon(getClass().getResource(ruta)));
        label.setBounds(x, y, w, h);
        return label;
    }
}