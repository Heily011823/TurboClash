package edu.autonoma.turboclash.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;

public class gameWindow extends JFrame {

    // ===== IMÁGENES =====
    private Image pista;
    private Image carro1;
    private Image carro2;
    private Image obstaculoImg;
    private Image corazon;

    // ===== FONDO =====
    private int fondoY1 = 0;
    private int fondoY2 = -720;
    private final int velocidadFondo = 6;

    // ===== CARROS =====
    private int carro1X = 470;
    private int carro1Y = 520;

    private int carro2X = 650;
    private int carro2Y = 520;

    // ===== OBSTÁCULOS =====
    private final ArrayList<Rectangle> obstaculos = new ArrayList<>();
    private final Random random = new Random();

    // ===== HUD =====
    private int vidas = 3;
    private int puntaje = 0;

    private Timer timer;

    public gameWindow() {
        initComponents();
        iniciarJuego();
    }

    private void initComponents() {
        setTitle("Turbo Clash - Juego");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cargar imágenes
        pista = new ImageIcon("src/edu/autonoma/turboclash/image/pista.png").getImage();
        carro1 = new ImageIcon("src/edu/autonoma/turboclash/image/carro1.png").getImage();
        carro2 = new ImageIcon("src/edu/autonoma/turboclash/image/carro2.png").getImage();
        obstaculoImg = new ImageIcon("src/edu/autonoma/turboclash/image/obstaculo.png").getImage();
        corazon = new ImageIcon("src/edu/autonoma/turboclash/image/corazon.png").getImage();

        // Generar obstáculos
        for (int i = 0; i < 6; i++) {
            crearObstaculo(-i * 150);
        }

        // Panel de dibujo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarTodo(g);
            }
        };

        panel.setFocusable(true);
        panel.setBackground(Color.BLACK);
        setContentPane(panel);
    }

    private void iniciarJuego() {
        timer = new Timer(30, (ActionEvent e) -> {
            actualizarJuego();
            repaint();
        });
        timer.start();
    }

    private void crearObstaculo(int y) {
        int minX = 360;
        int maxX = 820;
        int x = random.nextInt(maxX - minX + 1) + minX;

        obstaculos.add(new Rectangle(x, y, 55, 55));
    }

    private void actualizarJuego() {
        moverFondo();
        moverObstaculos();
        reciclarObstaculos();
        puntaje++;
    }

    private void moverFondo() {
        fondoY1 += velocidadFondo;
        fondoY2 += velocidadFondo;

        if (fondoY1 >= getHeight()) fondoY1 = -getHeight();
        if (fondoY2 >= getHeight()) fondoY2 = -getHeight();
    }

    private void moverObstaculos() {
        for (Rectangle r : obstaculos) {
            r.y += velocidadFondo;
        }
    }

    private void reciclarObstaculos() {
        for (int i = 0; i < obstaculos.size(); i++) {
            Rectangle r = obstaculos.get(i);

            if (r.y > getHeight()) {
                obstaculos.remove(i);
                crearObstaculo(-random.nextInt(300) - 100);
                i--;
            }
        }
    }

    private void dibujarTodo(Graphics g) {

        // ===== FONDO =====
        g.drawImage(pista, 0, fondoY1, getWidth(), getHeight(), this);
        g.drawImage(pista, 0, fondoY2, getWidth(), getHeight(), this);

        // ===== OBSTÁCULOS =====
        for (Rectangle r : obstaculos) {
            g.drawImage(obstaculoImg, r.x, r.y, r.width, r.height, this);
        }

        // ===== CARROS =====
        g.drawImage(carro1, carro1X, carro1Y, 90, 140, this);
        g.drawImage(carro2, carro2X, carro2Y, 90, 140, this);

        // ===== HUD =====
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(20, 15, 300, 60, 20, 20);

        // Corazones
        int x = 35;
        for (int i = 0; i < vidas; i++) {
            g2.drawImage(corazon, x, 25, 30, 30, this);
            x += 40;
        }

        // Puntaje
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Puntaje: " + puntaje, 160, 48);
    }

}