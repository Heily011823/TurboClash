package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;
import edu.autonoma.turboclash.presentation.view.EndGameWindowFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;
    private JLabel statusLabel;
    private Runnable onCountdownFinished;

    private JButton btnIrFin;

    private JButton btnClose;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<String, JLabel> healthLabels = new HashMap<>();

    private FondoAnimadoPanel fondoAnimadoPanel;

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

        btnIrFin = new JButton("Ir a fin");
        btnIrFin.setBounds(850, 20, 120, 30); // posición arriba derecha
        panel1.add(btnIrFin);

        btnClose = new JButton("X");
        btnClose.setBounds(1200, 20, 50, 50); // esquina superior derecha

        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBackground(new Color(150, 0, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 18));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel1.add(btnClose);

        btnClose.addActionListener(e -> System.exit(0));

        btnIrFin.addActionListener(e -> abrirVentanaFinal());
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

    public void updateCars(List<Player> players) {
        if (players == null) return;

        for (Player p : players) {
            if (p != null && p.getCar() != null) {
                updateCarPosition(p.getCar());
            }
        }

        panel1.repaint();
    }

    public void updateCarPosition(Car car) {
        if (car == null) return;

        JLabel lbl = carLabels.computeIfAbsent(car.getId(), id -> {
            JLabel newLbl = new JLabel(getIcon("/image/" + car.getCarImage(), 100, 50));
            panel1.add(newLbl);
            return newLbl;
        });

        lbl.setBounds((int) car.getX(), (int) car.getY(), 100, 50);
        lbl.setVisible(car.isActive());

        updateHealth(car, car.getLives());
    }

    public void updateHealth(Car car, int lives) {
        String carId = car.getId();

        for (int i = 0; i < 3; i++) {
            String heartKey = carId + "_heart_" + i;

            JLabel heart = healthLabels.computeIfAbsent(heartKey, id -> {
                JLabel lbl = new JLabel(getIcon("/image/Health.png", 25, 25));
                panel1.add(lbl);
                return lbl;
            });

            heart.setBounds((int) car.getX() + (i * 30),
                    (int) car.getY() - 30,
                    25, 25);

            heart.setVisible(i < lives && car.isActive());
        }
    }

    public void updateObstacles(List<Obstacle> obstacles) {
        obstacleLabels.values().forEach(lbl -> lbl.setVisible(false));

        if (obstacles == null) return;

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

        if (items == null) return;

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

    public void prepareRaceStart(List<Car> cars) {
        if (cars == null || cars.isEmpty()) return;

        final int startX = 80;
        final int[] lanesY = {100, 190, 280, 370};

        for (int i = 0; i < cars.size() && i < lanesY.length; i++) {
            Car car = cars.get(i);
            car.setPosition(startX, lanesY[i]);
            updateCarPosition(car);
        }

        panel1.repaint();
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
        if (fondoAnimadoPanel == null) return Integer.MAX_VALUE;
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
            return;
        }

        String primero = ranking.size() > 0 ? ranking.get(0).getName() : "";
        String segundo = ranking.size() > 1 ? ranking.get(1).getName() : "";
        String tercero = ranking.size() > 2 ? ranking.get(2).getName() : "";
        String cuarto = ranking.size() > 3 ? ranking.get(3).getName() : "";

        SwingUtilities.invokeLater(() ->
                new EndGameWindowFrame(primero, segundo, tercero, cuarto)
        );
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
        if (url == null) return new ImageIcon();

        Image scaled = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void abrirVentanaFinal() {
        String primero = "Jugador1";
        String segundo = "Jugador2";
        String tercero = "Jugador3";
        String cuarto = "Jugador4";

        new EndGameWindowFrame(primero, segundo, tercero, cuarto);
    }
}