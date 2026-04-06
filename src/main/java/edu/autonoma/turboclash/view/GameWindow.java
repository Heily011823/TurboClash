package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private KeyboardInput keyboardInput;
    private MouseInput mouseInput;

    private final List<JLabel> carLabels = new ArrayList<>();
    private final List<JLabel> healthLabels = new ArrayList<>();
    private final List<JLabel> obstacleLabels = new ArrayList<>();
    private final List<JLabel> itemLabels = new ArrayList<>();

    private final String[] SKINS = {
            "/image/Car_Blue.png", "/image/Car_Red.png",
            "/image/Car_Yellow.png", "/image/Car_Brown.png"
    };

    public GameWindow(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;

        if (panel1 != null) {
            panel1.setLayout(null);
            panel1.setFocusable(true);
            panel1.requestFocusInWindow();
            initKeyboard();
            initMouse();
        }

        initializeScoreUI();
    }

    private void initializeScoreUI() {
        if (Puntaje != null) {
            Puntaje.setForeground(Color.YELLOW);
            Puntaje.setFont(new Font("Arial", Font.BOLD, 24));
            Puntaje.setText("Score: 0");

            Puntaje.setBounds(20, 43, 200, 40);
        }
    }

    public void updateScore(int points) {
        if (this.Puntaje != null) {
            SwingUtilities.invokeLater(() -> {
                this.Puntaje.setText("Score: " + points);

                panel1.setComponentZOrder(this.Puntaje, 0);
                this.Puntaje.setSize(250, 40);

                this.Puntaje.setVisible(true);

                this.Puntaje.repaint();
            });
        }
    }
    public void updateHealth(int lives, Car car) {
        if (car == null || panel1 == null) return;

        if (healthLabels.isEmpty()) {
            java.net.URL imgUrl = getClass().getResource("/image/Health.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(new ImageIcon(imgUrl).getImage()
                        .getScaledInstance(25, 25, Image.SCALE_SMOOTH));

                for (int i = 0; i < 3; i++) {
                    JLabel heart = new JLabel(icon);
                    panel1.add(heart);
                    panel1.setComponentZOrder(heart, 1);
                    healthLabels.add(heart);
                }
            }
        }

        for (int i = 0; i < healthLabels.size(); i++) {
            JLabel heart = healthLabels.get(i);
            heart.setBounds((int)car.getX() + (i * 30), (int)car.getY() - 30, 25, 25);
            heart.setVisible(i < lives && car.isActive());
        }
    }

    public void updateItems(List<Item> items) {
        if (panel1 == null) return;

        while (itemLabels.size() < items.size()) {
            JLabel lbl = createScalableLabel("/image/Coin.png", 30, 30);
            panel1.add(lbl);
            itemLabels.add(lbl);
        }

        for (int i = 0; i < itemLabels.size(); i++) {
            JLabel lbl = itemLabels.get(i);
            if (i < items.size()) {
                Item item = items.get(i);
                lbl.setBounds((int)item.getX(), (int)item.getY(), 30, 30);
                lbl.setVisible(item.isVisible());
            } else {
                lbl.setVisible(false);
            }
        }
    }

    public void updateCars(List<Car> cars) {
        if (panel1 == null) return;

        while (carLabels.size() < cars.size()) {
            JLabel lbl = new JLabel();
            carLabels.add(lbl);
            panel1.add(lbl);
        }

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            JLabel lbl = carLabels.get(i);
            int skinIndex = Math.abs(car.getId().hashCode()) % SKINS.length;

            java.net.URL carUrl = getClass().getResource(SKINS[skinIndex]);
            if (carUrl != null) {
                ImageIcon icon = new ImageIcon(new ImageIcon(carUrl)
                        .getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH));
                lbl.setIcon(icon);
            }

            lbl.setBounds((int) car.getX(), (int) car.getY(), 100, 50);
            lbl.setVisible(car.isActive());
        }
    }


    public void updateObstacles(List<Obstacle> obstacles) {
        if (panel1 == null) return;

        while (obstacleLabels.size() < obstacles.size()) {
            JLabel lbl = new JLabel();
            panel1.add(lbl);
            obstacleLabels.add(lbl);
        }

        for (int i = 0; i < obstacleLabels.size(); i++) {
            JLabel lbl = obstacleLabels.get(i);
            if (i < obstacles.size()) {
                Obstacle obs = obstacles.get(i);

                // --- CAMBIO DE IMAGEN DINÁMICO ---
                String imagePath = "/image/Cone.png"; // Por defecto
                if ("OIL".equals(obs.getType())) imagePath = "/image/Oil_Spill.png";
                else if ("BARRIER".equals(obs.getType())) imagePath = "/image/Barrier.png";

                // Aplicamos la imagen correcta
                lbl.setIcon(getIcon(imagePath, 45, 45));

                lbl.setBounds((int)obs.getX(), (int)obs.getY(), 45, 45);
                lbl.setVisible(obs.isVisible());
            } else {
                lbl.setVisible(false);
            }
        }
    }

    // Helper para no repetir código de carga de imagen
    private ImageIcon getIcon(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return new ImageIcon();
        return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    private JLabel createScalableLabel(String path, int width, int height) {
        return new JLabel(getIcon(path, width, height));
    }

    private void initKeyboard() {
        panel1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { processKey(e.getKeyCode(), true); }
            @Override
            public void keyReleased(KeyEvent e) { processKey(e.getKeyCode(), false); }

            private void processKey(int code, boolean pressed) {
                switch (code) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP: keyboardInput.setUp(pressed); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN: keyboardInput.setDown(pressed); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT: keyboardInput.setLeft(pressed); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: keyboardInput.setRight(pressed); break;
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
}