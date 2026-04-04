package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;
import edu.autonoma.turboclash.model.Car;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWindow {

    public JPanel panel1;

    private JLabel Vidas;
    private JLabel Puntaje;

    private KeyboardInput keyboardInput;
    private MouseInput mouseInput;

    private List<JLabel> listaObstaculos = new ArrayList<>();
    private List<JLabel> listaMonedas = new ArrayList<>();
    private List<JLabel> carros = new ArrayList<>();

    private Random random = new Random();
    private int velocidadJuego = 5;

    private Timer motorJuego;

    private String[] imagenesObstaculos = {
            "/image/Barrier.png",
            "/image/Cone.png",
            "/image/Oil_Spill.png"
    };

    public GameWindow(KeyboardInput keyboardInput, MouseInput mouseInput) {

        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;

        panel1.setLayout(null);
        panel1.setOpaque(false);
        panel1.setFocusable(true);

        initKeyboard();
        initMouse();

        generarObstaculos(3);
        generarMonedas(2);

        motorJuego = new Timer(20, e -> actualizarPosiciones());
        motorJuego.start();

        // 🔥 CLAVE: dar foco para que funcione teclado
        SwingUtilities.invokeLater(() -> panel1.requestFocusInWindow());
    }

    // ---------------- INPUT ----------------

    private void initKeyboard() {
        panel1.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        keyboardInput.setUp(true);
                        break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        keyboardInput.setDown(true);
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        keyboardInput.setLeft(true);
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        keyboardInput.setRight(true);
                        break;
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        keyboardInput.setUp(false);
                        break;

                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        keyboardInput.setDown(false);
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        keyboardInput.setLeft(false);
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        keyboardInput.setRight(false);
                        break;
                }
            }
        });
    }

    private void initMouse() {
        panel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }
        });
    }

    // ---------------- CARROS ----------------

    public void actualizarCarros(List<Car> cars) {

        // Crear carros si faltan
        while (carros.size() < cars.size()) {

            String[] skins = {
                    "/image/Car_Blue.png",
                    "/image/Car_Red.png",
                    "/image/Car_Yellow.png",
                    "/image/Car_Brown.png"
            };

            String ruta = skins[carros.size() % skins.length];

            JLabel nuevo = crearLabelRecurso(ruta, 0, 0, 100, 50);
            carros.add(nuevo);
            panel1.add(nuevo);
        }

        // Actualizar posiciones
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            JLabel label = carros.get(i);

            label.setLocation((int) car.getPosX(), (int) car.getPosY());
        }
    }

    // ---------------- OBSTÁCULOS ----------------

    private void generarObstaculos(int cantidad) {
        int[] carrilesY = {90, 180, 290, 450};

        for (int i = 0; i < cantidad; i++) {
            String ruta = imagenesObstaculos[random.nextInt(imagenesObstaculos.length)];

            JLabel obs = crearLabelRecurso(
                    ruta,
                    800 + (i * 400),
                    carrilesY[random.nextInt(4)],
                    60,
                    60
            );

            panel1.add(obs);
            listaObstaculos.add(obs);
        }
    }

    private void generarMonedas(int cantidad) {
        int[] carrilesY = {90, 180, 290, 450};

        for (int i = 0; i < cantidad; i++) {

            JLabel moneda = crearLabelRecurso(
                    "/image/Coin.png",
                    1200 + (i * 500),
                    carrilesY[random.nextInt(4)],
                    40,
                    40
            );

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

    // ---------------- UTIL ----------------

    private JLabel crearLabelRecurso(String ruta, int x, int y, int w, int h) {
        JLabel label = new JLabel(new ImageIcon(getClass().getResource(ruta)));
        label.setBounds(x, y, w, h);
        return label;
    }
}