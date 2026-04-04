package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.model.Car;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class GameWindow {

    public JPanel panel1;
    private JLabel Puntaje;

    private KeyboardInput keyboardInput;
    private MouseInput mouseInput;

    private List<JLabel> carrosLabels = new ArrayList<>();
    private List<JLabel> corazones = new ArrayList<>();
    private List<JLabel> obstaculosLabels = new ArrayList<>();

    private final String[] SKINS = {
            "/image/Car_Blue.png", "/image/Car_Red.png",
            "/image/Car_Yellow.png", "/image/Car_Brown.png"
    };

    public GameWindow(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;

        panel1.setLayout(null);
        panel1.setFocusable(true);
        panel1.requestFocusInWindow();

        initKeyboard();
        initMouse();
    }

    // -------------------- PUNTAJE --------------------

    public void actualizarPuntaje(int puntos) {
        Puntaje.setText("Puntaje: " + puntos);
    }

    // -------------------- CORAZONES --------------------

    public void actualizarCorazones(int vidas, Car car) {
        if (car == null) return;

        if (corazones.isEmpty()) {
            ImageIcon icon = new ImageIcon(getClass().getResource("/image/Health.png"));

            for (int i = 0; i < 3; i++) {
                JLabel c = new JLabel(new ImageIcon(icon.getImage()
                        .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
                panel1.add(c);
                panel1.setComponentZOrder(c, 0);
                corazones.add(c);
            }
        }

        int x = (int) car.getX();
        int y = (int) car.getY();

        for (int i = 0; i < corazones.size(); i++) {
            JLabel c = corazones.get(i);
            c.setBounds(x + (i * 30), y - 30, 25, 25);
            c.setVisible(i < vidas);
        }
    }

    // -------------------- CARROS --------------------

    public void actualizarCarros(List<Car> cars) {

        while (carrosLabels.size() < cars.size()) {
            JLabel lbl = new JLabel();
            carrosLabels.add(lbl);
            panel1.add(lbl);
        }

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            JLabel lbl = carrosLabels.get(i);

            int idNum = Math.abs(car.getId().hashCode());

            ImageIcon icon = new ImageIcon(getClass().getResource(
                    SKINS[idNum % SKINS.length]
            ));

            lbl.setIcon(new ImageIcon(icon.getImage()
                    .getScaledInstance(100, 50, Image.SCALE_SMOOTH)));

            lbl.setBounds((int) car.getX(), (int) car.getY(), 100, 50);
        }
    }

    // -------------------- OBSTÁCULOS --------------------

    public void actualizarObstaculos(List<Point> obs) {

        while (obstaculosLabels.size() < obs.size()) {
            JLabel lbl = new JLabel(new ImageIcon(
                    new ImageIcon(getClass().getResource("/image/Oil_Spill.png"))
                            .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
            ));
            panel1.add(lbl);
            panel1.setComponentZOrder(lbl, 2);
            obstaculosLabels.add(lbl);
        }

        for (int i = 0; i < obstaculosLabels.size(); i++) {
            JLabel lbl = obstaculosLabels.get(i);

            if (i < obs.size()) {
                Point p = obs.get(i);
                lbl.setBounds(p.x, p.y, 40, 40);
                lbl.setVisible(true);
            } else {
                lbl.setVisible(false);
            }
        }
    }

    // -------------------- INPUT --------------------

    private void initKeyboard() {
        panel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) { handle(e.getKeyCode(), true); }
            public void keyReleased(KeyEvent e) { handle(e.getKeyCode(), false); }

            private void handle(int k, boolean s) {
                switch (k) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP: keyboardInput.setUp(s); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN: keyboardInput.setDown(s); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT: keyboardInput.setLeft(s); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: keyboardInput.setRight(s); break;
                }
            }
        });
    }

    private void initMouse() {
        panel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseInput.setTarget(e.getX(), e.getY());
            }
        });
    }
}