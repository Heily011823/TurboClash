package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.Item;

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
    private FondoAnimadoPanel fondo;
    private List<JLabel> carrosLabels = new ArrayList<>();
    private List<JLabel> corazones = new ArrayList<>();
    private List<JLabel> obstaculosLabels = new ArrayList<>();
    private List<JLabel> itemsLabels = new ArrayList<>();

    private final String[] SKINS = {
            "/image/Car_Blue.png", "/image/Car_Red.png",
            "/image/Car_Yellow.png", "/image/Car_Brown.png"
    };
    public void setFondo(FondoAnimadoPanel fondo) {
        this.fondo = fondo;
    }

    public GameWindow(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;


        panel1.setLayout(null);

        panel1.setFocusable(true);
        panel1.requestFocusInWindow();

        initKeyboard();
        initMouse();
    }

    public void actualizarPuntaje(int puntos) {
        if (Puntaje != null) {
            Puntaje.setText("Puntaje: " + puntos);
        }
    }

    public void actualizarCorazones(int vidas, Car car) {
        if (car == null || panel1 == null) return;

        if (corazones.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/image/Health.png"));
                for (int i = 0; i < 3; i++) {
                    JLabel c = new JLabel(new ImageIcon(icon.getImage()
                            .getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
                    panel1.add(c);
                    panel1.setComponentZOrder(c, 0); // Al frente
                    corazones.add(c);
                }
            } catch (Exception e) {
                System.err.println("Error cargando Health.png: " + e.getMessage());
            }
        }

        for (int i = 0; i < corazones.size(); i++) {
            JLabel c = corazones.get(i);
            c.setBounds((int) car.getX() + (i * 30), (int) car.getY() - 30, 25, 25);
            c.setVisible(i < vidas);
        }
    }

    public void actualizarCarros(List<Car> cars) {
        while (carrosLabels.size() < cars.size()) {
            JLabel lbl = new JLabel();
            carrosLabels.add(lbl);
            panel1.add(lbl);
            panel1.setComponentZOrder(lbl, 1);
        }
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            JLabel lbl = carrosLabels.get(i);
            int idNum = Math.abs(car.getId().hashCode());
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(SKINS[idNum % SKINS.length]));
                lbl.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
                int x = (int) car.getX();
                int y = (int) car.getY();
                int anchoCarro = 100;
                int altoCarro = 50;
                int anchoPanel = panel1.getWidth();
                int altoPanel = panel1.getHeight();
                if (x < 0) x = 0;
                if (y < 50) y = 50;
                if (x > anchoPanel - anchoCarro) x = anchoPanel - anchoCarro;
                if (y > altoPanel - altoCarro) y = altoPanel - altoCarro;
                lbl.setBounds(x, y, anchoCarro, altoCarro);
                lbl.setVisible(true);
                // FINAL DEL JUEGO CUANDO EL CARRO PASE TODA LA META
                if (fondo != null && fondo.isMetaVisible() && !fondo.isJuegoTerminado()) {
                    int metaX = fondo.getMetaX();
                    int anchoMeta = 200;
                    if (x + anchoCarro >= metaX) {
                        fondo.terminarJuego();
                    }
                }
            } catch (Exception e) {
                lbl.setText("CAR");
            }
        }
    }
    public void actualizarItems(List<Item> items) {
        while (itemsLabels.size() < items.size()) {
            JLabel lbl = new JLabel();
            java.net.URL url = getClass().getResource("/image/Coin.png");
            if (url != null) {
                lbl.setIcon(new ImageIcon(new ImageIcon(url).getImage()
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
            }
            panel1.add(lbl);
            panel1.setComponentZOrder(lbl, 2);
            itemsLabels.add(lbl);
        }

        for (int i = 0; i < itemsLabels.size(); i++) {
            JLabel lbl = itemsLabels.get(i);
            if (i < items.size() && items.get(i).isVisible()) {
                Item item = items.get(i);
                lbl.setBounds((int) item.getX(), (int) item.getY(), 30, 30);
                lbl.setVisible(true);
            } else {
                lbl.setVisible(false);
            }
        }
    }

    public void actualizarObstaculos(List<Point> obs) {
        while (obstaculosLabels.size() < obs.size()) {
            try {
                JLabel lbl = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/image/Oil_Spill.png"))
                        .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
                panel1.add(lbl);
                panel1.setComponentZOrder(lbl, 3);
                obstaculosLabels.add(lbl);
            } catch (Exception e) {
                System.err.println("Error cargando Oil_Spill.png");
            }
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


        panel1.revalidate();
        panel1.repaint();
    }

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