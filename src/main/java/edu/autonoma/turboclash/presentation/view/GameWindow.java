package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa y organiza la vista {@code GameWindow} en la capa de presentacion.
 */
public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;
    private JLabel statusLabel;
    private Runnable onCountdownFinished;
    private JButton btnClose;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<String, JLabel> healthLabels = new HashMap<>();

    private FondoAnimadoPanel fondoAnimadoPanel;

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final int START_X = 80;

    // 4 carriles iniciales, bien separados
    private static final int[] START_LANES_Y = {80, 220, 360, 500};

    public GameWindow() {
        if (panel1 == null) {
            panel1 = new JPanel(null);
        }

        panel1.setLayout(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(1000, 700));

        Puntaje = createScoreLabel();
        panel1.add(Puntaje);

        statusLabel = createStatusLabel();
        panel1.add(statusLabel);

        initializeCountdownUI();

        btnClose = new JButton("X");
        btnClose.setBounds(930, 20, 55, 55);
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBackground(new Color(150, 0, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 18));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel1.add(btnClose);

        btnClose.addActionListener(e -> System.exit(0));
    }

    public void setBackgroundPanel(FondoAnimadoPanel fondoAnimadoPanel) {
        this.fondoAnimadoPanel = fondoAnimadoPanel;
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void requestGameFocus() {
        panel1.requestFocusInWindow();
    }

    public void setOnCountdownFinished(Runnable action) {
        this.onCountdownFinished = action;
    }

    public void showWaitingPlayers() {
        statusLabel.setText("Esperando al menos 2 jugadores...");
        statusLabel.setVisible(true);
    }

    public void showGameStarted() {
        statusLabel.setVisible(false);
    }

    public void updateScore(int points) {
        Puntaje.setText("Puntaje: " + points);
    }

    /**
     * IMPORTANTE:
     * Aqui NO se fuerza el carril.
     * Solo se dibuja el carro en la posicion real que tenga.
     */
    public void updateCars(List<Player> players) {
        if (players == null) {
            return;
        }

        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            updateCarPosition(player.getCar());
        }

        panel1.revalidate();
        panel1.repaint();
    }

    /**
     * Dibuja el carro donde realmente esta.
     */
    public void updateCarPosition(Car car) {
        if (car == null || car.getId() == null) {
            return;
        }

        JLabel lbl = carLabels.computeIfAbsent(car.getId(), id -> {
            String imagePath = normalizeCarImagePath(car.getCarImage());
            JLabel newLbl = new JLabel(getIcon(imagePath, CAR_WIDTH, CAR_HEIGHT));
            newLbl.setOpaque(false);
            panel1.add(newLbl);
            panel1.setComponentZOrder(newLbl, 0);
            return newLbl;
        });

        int drawX = Math.max(0, (int) car.getX());
        int drawY = Math.max(0, (int) car.getY());

        lbl.setBounds(drawX, drawY, CAR_WIDTH, CAR_HEIGHT);
        lbl.setVisible(car.isActive());

        updateHealthVisual(drawX, drawY, car.getId(), car.isActive(), car.getLives());
    }

    public void updateHealth(Car car, int lives) {
        if (car == null || car.getId() == null) {
            return;
        }

        updateHealthVisual((int) car.getX(), (int) car.getY(), car.getId(), car.isActive(), lives);
    }

    private void updateHealthVisual(int x, int y, String carId, boolean active, int lives) {
        for (int i = 0; i < 3; i++) {
            String heartKey = carId + "_heart_" + i;

            JLabel heart = healthLabels.computeIfAbsent(heartKey, id -> {
                JLabel lbl = new JLabel(getIcon("/image/Health.png", 25, 25));
                panel1.add(lbl);
                return lbl;
            });

            heart.setBounds(x + (i * 28), y - 30, 25, 25);
            heart.setVisible(i < lives && active);
        }
    }

    public void updateObstacles(List<Obstacle> obstacles) {
        obstacleLabels.values().forEach(lbl -> lbl.setVisible(false));

        if (obstacles == null) {
            return;
        }

        for (Obstacle obs : obstacles) {
            JLabel lbl = obstacleLabels.computeIfAbsent(obs.getId(), id -> {
                String path;
                switch (obs.getType()) {
                    case OIL -> path = "/image/Oil_Spill.png";
                    case BARRIER -> path = "/image/Barrier.png";
                    default -> path = "/image/Cone.png";
                }
                JLabel newLbl = new JLabel(getIcon(path, 45, 45));
                panel1.add(newLbl);
                return newLbl;
            });

            lbl.setBounds((int) obs.getX(), (int) obs.getY(), 45, 45);
            lbl.setVisible(obs.isVisible());
        }
    }

    public void updateItems(List<Item> items) {
        itemLabels.values().forEach(lbl -> lbl.setVisible(false));

        if (items == null) {
            return;
        }

        for (Item item : items) {
            JLabel lbl = itemLabels.computeIfAbsent(item.getId(), id -> {
                JLabel newLbl = new JLabel(getIcon("/image/Coin.png", 30, 30));
                panel1.add(newLbl);
                return newLbl;
            });

            lbl.setBounds((int) item.getX(), (int) item.getY(), 30, 30);
            lbl.setVisible(item.isVisible());
        }
    }

    /**
     * SOLO acomoda la salida inicial.
     * Cada jugador sale en una posicion diferente.
     * Despues se pueden mover libremente.
     */
    public void prepareRaceStart(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }

        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            Car car = player.getCar();
            car.setPosition(START_X, getStartLaneY(player));
            updateCarPosition(car);
        }

        panel1.repaint();
    }

    /**
     * Asigna 1 de 4 carriles iniciales.
     */
    private int getStartLaneY(Player player) {
        if (player == null || player.getId() == null) {
            return START_LANES_Y[0];
        }

        String id = player.getId().trim();

        switch (id) {
            case "5001":
            case "player1":
            case "jugador1":
                return START_LANES_Y[0];

            case "5002":
            case "player2":
            case "jugador2":
                return START_LANES_Y[1];

            case "5003":
            case "player3":
            case "jugador3":
                return START_LANES_Y[2];

            case "5004":
            case "player4":
            case "jugador4":
                return START_LANES_Y[3];

            default:
                return START_LANES_Y[Math.abs(id.hashCode()) % START_LANES_Y.length];
        }
    }

    public void startCountdown() {
        final int[] seconds = {3};

        countdownLabel.setText("3");
        countdownLabel.setVisible(true);

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            seconds[0]--;

            if (seconds[0] > 0) {
                countdownLabel.setText(String.valueOf(seconds[0]));
                SoundManager.getInstance().playEffect(SoundManager.Sound.START);
            } else if (seconds[0] == 0) {
                countdownLabel.setText("GO!");
            } else {
                timer.stop();
                countdownLabel.setVisible(false);
                if (onCountdownFinished != null) {
                    onCountdownFinished.run();
                }
            }
        });

        timer.start();
    }

    public int getMetaX() {
        if (fondoAnimadoPanel == null) {
            return Integer.MAX_VALUE;
        }
        return fondoAnimadoPanel.getMetaX();
    }

    public boolean isMetaVisible() {
        return fondoAnimadoPanel != null && fondoAnimadoPanel.isMetaVisible();
    }

    public boolean isBackgroundFinished() {
        return fondoAnimadoPanel != null && fondoAnimadoPanel.isJuegoTerminado();
    }

    public void showGameResult(List<Player> ranking) {
        if (fondoAnimadoPanel != null) {
            fondoAnimadoPanel.terminarJuego(ranking);
        }
    }

    private JLabel createScoreLabel() {
        JLabel label = new JLabel("Puntaje: 0");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBounds(20, 40, 250, 40);
        return label;
    }

    private JLabel createStatusLabel() {
        JLabel label = new JLabel("Esperando al menos 2 jugadores...");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBounds(20, 80, 450, 40);
        label.setVisible(false);
        return label;
    }

    private void initializeCountdownUI() {
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBounds(400, 250, 200, 80);
        countdownLabel.setVisible(false);
        panel1.add(countdownLabel);
    }

    private ImageIcon getIcon(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("No se encontró imagen: " + path);
            return new ImageIcon();
        }

        Image scaled = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private String normalizeCarImagePath(String carImage) {
        if (carImage == null || carImage.isBlank()) {
            return "/image/Car_Blue.png";
        }

        String value = carImage.trim();

        if (value.startsWith("/image/")) {
            return value;
        }

        if (value.startsWith("/")) {
            return value;
        }

        return "/image/" + value;
    }
}