package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.util.Random;

public class GameWindow {
    public JPanel panel1;
    private JLabel Vidas;
    private JLabel obstaculo;
    private JLabel monedas;
    private JLabel Puntaje;
    private JLabel carro1, carro2, carro3, carro4;

    private Random random = new Random();

    private String[] imagenesObstaculos = {
            "/image/Barrier.png",
            "/image/Cone.png",
            "/image/Oil_Spill.png"
    };

    public GameWindow() {

        panel1.setLayout(null);

        colocarCarros();
        generarObstaculos(6);
        generarMonedas(4);
    }

    private void colocarCarros() {
        carro1 = new JLabel(new ImageIcon(getClass().getResource("/image/Car_Blue.png")));
        carro2 = new JLabel(new ImageIcon(getClass().getResource("/image/Car_Brown.png")));
        carro3 = new JLabel(new ImageIcon(getClass().getResource("/image/Car_Red.png")));
        carro4 = new JLabel(new ImageIcon(getClass().getResource("/image/Car_Yellow.png")));

        carro1.setBounds(50, 90, 100, 50);
        carro2.setBounds(50, 180, 100, 50);
        carro3.setBounds(50, 290, 100, 50);
        carro4.setBounds(50, 450, 100, 50);

        panel1.add(carro1);
        panel1.add(carro2);
        panel1.add(carro3);
        panel1.add(carro4);
    }

    private void generarObstaculos(int cantidad) {

        int[] carrilesY = {100, 180, 260, 340};

        for (int i = 0; i < cantidad; i++) {

            String ruta = imagenesObstaculos[random.nextInt(imagenesObstaculos.length)];

            JLabel obstaculo = new JLabel(new ImageIcon(getClass().getResource(ruta)));
            int y = carrilesY[random.nextInt(carrilesY.length)];
            int x = 400 + (i * 120);
            obstaculo.setBounds(x, y, 60, 60);
            panel1.add(obstaculo);
        }

        panel1.repaint();
    }

    private void generarMonedas(int cantidad) {

        int[] carrilesY = {100, 180, 260, 340};

        for (int i = 0; i < cantidad; i++) {

            JLabel moneda = new JLabel(
                    new ImageIcon(getClass().getResource("/image/Coin.png"))
            );

            int y = carrilesY[random.nextInt(carrilesY.length)];
            int x = 450 + (i * 150);

            moneda.setBounds(x, y, 40, 40);

            panel1.add(moneda);
        }

        panel1.repaint();
    }


}