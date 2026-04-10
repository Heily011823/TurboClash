package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;
    private JLabel statusLabel;
    private JButton btnClose;
    private Runnable onCountdownFinished;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<String, JLabel> heartLabels = new HashMap<>();
    private final Map<String, JLabel> nameLabels = new HashMap<>();
    private final Map<String, ImageIcon> iconCache = new HashMap<>();

    private FondoAnimadoPanel fondoAnimadoPanel;
    private Timer synchronizedCountdownTimer;

    private static final int BASE_WIDTH = 1000;
    private static final int BASE_HEIGHT = 700;
    private static final int START_X = 80;

    private boolean gameStarted = false;
    private String localPlayerId;
    private String localPlayerName;

    public GameWindow() {
        if (panel1 == null) {
            panel1 = new JPanel(null);
        }

        panel1.setLayout(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        Puntaje = createScoreLabel();
        panel1.add(Puntaje);

        statusLabel = createStatusLabel();
        panel1.add(statusLabel);

        initializeCountdownUI();

        btnClose = new JButton("X");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setBackground(new Color(150, 0, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));
        panel1.add(btnClose);

        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateResponsiveLayout();
                iconCache.clear();
            }
        });

        SwingUtilities.invokeLater(this::updateResponsiveLayout);
    }

    public void setBackgroundPanel(FondoAnimadoPanel fondoAnimadoPanel) {
        this.fondoAnimadoPanel = fondoAnimadoPanel;
    }

    public FondoAnimadoPanel getBackgroundPanel() {
        return fondoAnimadoPanel;
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void requestGameFocus() {
        panel1.requestFocusInWindow();
    }

    public void setLocalPlayer(Player localPlayer) {
        if (localPlayer == null) {
            this.localPlayerId = null;
            this.localPlayerName = null;
            return;
        }

        this.localPlayerId = localPlayer.getId();
        this.localPlayerName = localPlayer.getName();
    }

    public void showWaitingPlayers() {
        gameStarted = false;
        statusLabel.setText("Esperando jugadores...");
        statusLabel.setVisible(true);
        panel1.repaint();
    }

    public void showWaitingPlayers(int connectedPlayers, int minPlayers) {
        gameStarted = false;
        statusLabel.setText("Esperando jugadores... " + connectedPlayers + "/" + minPlayers + " minimo");
        statusLabel.setVisible(true);
        panel1.repaint();
    }

    public void showGameStarted() {
        gameStarted = true;
        statusLabel.setVisible(false);
        panel1.repaint();
    }

    public void setOnCountdownFinished(Runnable onCountdownFinished) {
        this.onCountdownFinished = onCountdownFinished;
    }

    public void startSynchronizedCountdown(long scheduledStartTime) {
        if (synchronizedCountdownTimer != null) {
            synchronizedCountdownTimer.stop();
        }

        gameStarted = false;
        countdownLabel.setVisible(true);
        synchronizedCountdownTimer = new Timer(100, e -> {
            long remainingMillis = scheduledStartTime - System.currentTimeMillis();
            if (remainingMillis <= 0) {
                countdownLabel.setVisible(false);
                ((Timer) e.getSource()).stop();
                showGameStarted();
                if (onCountdownFinished != null) {
                    onCountdownFinished.run();
                }
                return;
            }

            int seconds = Math.max(1, (int) Math.ceil(remainingMillis / 1000.0));
            countdownLabel.setText(String.valueOf(seconds));
        });
        synchronizedCountdownTimer.start();
    }

    public void startCountdown() {
        startSynchronizedCountdown(System.currentTimeMillis() + 3000L);
    }

    public void updateScore(int points) {
        Puntaje.setText("Puntaje: " + points);
    }

    public void updateCars(List<Player> players) {
        if (players == null) {
            return;
        }

        Set<String> currentCars = new HashSet<>();
        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            currentCars.add(player.getCar().getId());
            boolean visible = (gameStarted || isLocalPlayer(player)) && !player.isEliminated();
            updateCarPosition(player, visible);
        }

        hideMissingCars(currentCars);
        panel1.repaint();
    }

    public void updateObstacles(List<Obstacle> obstacles) {
        obstacleLabels.values().forEach(lbl -> lbl.setVisible(false));
        if (obstacles == null) {
            return;
        }

        int obstacleSize = Math.max(28, scale(45));
        for (Obstacle obstacle : obstacles) {
            JLabel label = obstacleLabels.computeIfAbsent(obstacle.getId(), id -> {
                JLabel newLabel = new JLabel();
                panel1.add(newLabel);
                return newLabel;
            });

            String path = switch (obstacle.getType()) {
                case OIL -> "/image/Oil_Spill.png";
                case BARRIER -> "/image/Barrier.png";
                default -> "/image/Cone.png";
            };
            label.setIcon(getIcon(path, obstacleSize, obstacleSize));
            label.setBounds(scaleX((int) obstacle.getX()), scaleY((int) obstacle.getY()), obstacleSize, obstacleSize);
            label.setVisible(obstacle.isVisible());
        }
    }

    public void updateItems(List<Item> items) {
        itemLabels.values().forEach(lbl -> lbl.setVisible(false));
        if (items == null) {
            return;
        }

        int itemSize = Math.max(20, scale(30));
        for (Item item : items) {
            JLabel label = itemLabels.computeIfAbsent(item.getId(), id -> {
                JLabel newLabel = new JLabel();
                panel1.add(newLabel);
                return newLabel;
            });
            label.setIcon(getIcon("/image/Coin.png", itemSize, itemSize));
            label.setBounds(scaleX((int) item.getX()), scaleY((int) item.getY()), itemSize, itemSize);
            label.setVisible(item.isVisible());
        }
    }

    public void prepareRaceStart(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }

        int[] lanes = {140, 280, 420, 560};
        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            Car car = player.getCar();
            if (car.getX() <= 0) {
                car.setPosition(START_X, getStartLaneY(player, lanes));
            } else if (car.getY() <= 0) {
                car.setPosition(car.getX(), getStartLaneY(player, lanes));
            }
        }
    }

    public int getMetaX() {
        return fondoAnimadoPanel == null ? Integer.MAX_VALUE : fondoAnimadoPanel.getMetaX();
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

    private void updateCarPosition(Player player, boolean visible) {
        Car car = player.getCar();
        if (car == null || car.getId() == null) {
            return;
        }

        int carWidth = getResponsiveCarWidth();
        int carHeight = getResponsiveCarHeight();
        JLabel label = carLabels.computeIfAbsent(car.getId(), id -> {
            JLabel newLabel = new JLabel();
            newLabel.setOpaque(false);
            panel1.add(newLabel);
            panel1.setComponentZOrder(newLabel, 0);
            return newLabel;
        });

        label.setIcon(getIcon(normalizeCarImagePath(car.getCarImage()), carWidth, carHeight));

        int drawX = Math.max(0, scaleX((int) car.getX()));
        int drawY = Math.max(0, scaleY((int) car.getY()));
        boolean active = visible && car.isActive();

        label.setBounds(drawX, drawY, carWidth, carHeight);
        label.setVisible(active);
        updateHealthVisual(drawX, drawY, car.getId(), active, car.getLives());
        updateNameVisual(drawX, drawY, player.getName(), car.getId(), active);
    }

    private void updateHealthVisual(int x, int y, String carId, boolean active, int lives) {
        int heartSize = Math.max(14, scale(20));
        int spacing = Math.max(16, scale(22));
        int offsetY = Math.max(10, scale(28));

        for (int i = 0; i < 3; i++) {
            String heartKey = carId + "_heart_" + i;
            JLabel heart = heartLabels.computeIfAbsent(heartKey, id -> {
                JLabel label = new JLabel();
                panel1.add(label);
                return label;
            });
            heart.setIcon(getIcon("/image/Health.png", heartSize, heartSize));
            heart.setBounds(x + (i * spacing), y - offsetY, heartSize, heartSize);
            heart.setVisible(i < lives && active);
        }
    }

    private void updateNameVisual(int x, int y, String playerName, String carId, boolean active) {
        int fontSize = Math.max(10, scale(14));
        int width = Math.max(90, scale(130));
        int height = Math.max(18, scale(20));
        int offsetY = Math.max(28, scale(50));

        JLabel nameLabel = nameLabels.computeIfAbsent(carId, id -> {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            panel1.add(label);
            return label;
        });

        nameLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        nameLabel.setText(playerName != null && !playerName.isBlank() ? playerName : "Jugador");
        nameLabel.setBounds(x - scale(10), y - offsetY, width, height);
        nameLabel.setVisible(active);
    }

    private void hideMissingCars(Set<String> currentCars) {
        for (Map.Entry<String, JLabel> entry : carLabels.entrySet()) {
            if (currentCars.contains(entry.getKey())) {
                continue;
            }

            entry.getValue().setVisible(false);
            JLabel nameLabel = nameLabels.get(entry.getKey());
            if (nameLabel != null) {
                nameLabel.setVisible(false);
            }
            for (int i = 0; i < 3; i++) {
                JLabel heartLabel = heartLabels.get(entry.getKey() + "_heart_" + i);
                if (heartLabel != null) {
                    heartLabel.setVisible(false);
                }
            }
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
        JLabel label = new JLabel("Esperando jugadores...");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBounds(100, 120, 450, 40);
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

    private void updateResponsiveLayout() {
        int panelWidth = panel1.getWidth() > 0 ? panel1.getWidth() : BASE_WIDTH;
        int panelHeight = panel1.getHeight() > 0 ? panel1.getHeight() : BASE_HEIGHT;

        int btnSize = scale(55);
        int marginRight = scale(15);
        int topMargin = scale(20);
        btnClose.setBounds(panelWidth - btnSize - marginRight, topMargin, btnSize, btnSize);
        btnClose.setFont(new Font("Arial", Font.BOLD, Math.max(12, scale(18))));

        Puntaje.setBounds(scaleX(20), scaleY(40), Math.max(180, scaleX(260)), Math.max(25, scaleY(40)));
        Puntaje.setFont(new Font("Arial", Font.BOLD, Math.max(14, scale(24))));

        int statusWidth = Math.max(260, scale(500));
        int statusHeight = Math.max(30, scale(40));
        statusLabel.setBounds(panelWidth / 2 - statusWidth / 2, panelHeight / 2 - statusHeight / 2, statusWidth, statusHeight);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0, 0, 0, 150));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, Math.max(14, scale(22))));

        countdownLabel.setBounds(panelWidth / 2 - scale(100), panelHeight / 2 - scale(40), Math.max(120, scale(200)), Math.max(50, scale(80)));
        countdownLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, scale(48))));
    }

    private int getResponsiveCarWidth() {
        return Math.max(60, scale(100));
    }

    private int getResponsiveCarHeight() {
        return Math.max(30, scale(50));
    }

    private int scale(int baseValue) {
        int currentWidth = panel1.getWidth() > 0 ? panel1.getWidth() : BASE_WIDTH;
        int currentHeight = panel1.getHeight() > 0 ? panel1.getHeight() : BASE_HEIGHT;
        double scaleX = currentWidth / (double) BASE_WIDTH;
        double scaleY = currentHeight / (double) BASE_HEIGHT;
        return Math.max(1, (int) Math.round(baseValue * Math.min(scaleX, scaleY)));
    }

    private int scaleX(int baseValue) {
        int currentWidth = panel1.getWidth() > 0 ? panel1.getWidth() : BASE_WIDTH;
        return (int) Math.round(baseValue * (currentWidth / (double) BASE_WIDTH));
    }

    private int scaleY(int baseValue) {
        int currentHeight = panel1.getHeight() > 0 ? panel1.getHeight() : BASE_HEIGHT;
        return (int) Math.round(baseValue * (currentHeight / (double) BASE_HEIGHT));
    }

    private ImageIcon getIcon(String path, int width, int height) {
        String cacheKey = path + "|" + width + "|" + height;
        ImageIcon cached = iconCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        java.net.URL url = getClass().getResource(path);
        if (url == null) {
            return new ImageIcon();
        }

        Image scaled = new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);
        iconCache.put(cacheKey, icon);
        return icon;
    }

    private String normalizeCarImagePath(String carImage) {
        if (carImage == null || carImage.isBlank()) {
            return "/image/Car_Blue.png";
        }
        if (carImage.startsWith("/")) {
            return carImage;
        }
        return "/image/" + carImage.trim();
    }

    private int getStartLaneY(Player player, int[] lanes) {
        if (player == null || player.getId() == null) {
            return lanes[0];
        }

        return switch (player.getId().trim()) {
            case "5001", "player1", "jugador1" -> lanes[0];
            case "5002", "player2", "jugador2" -> lanes[1];
            case "5003", "player3", "jugador3" -> lanes[2];
            case "5004", "player4", "jugador4" -> lanes[3];
            default -> lanes[Math.abs(player.getId().hashCode()) % lanes.length];
        };
    }

    private boolean isLocalPlayer(Player player) {
        if (player == null) {
            return false;
        }

        boolean sameId = localPlayerId != null && player.getId() != null && localPlayerId.equals(player.getId());
        boolean sameName = localPlayerName != null && player.getName() != null && localPlayerName.equalsIgnoreCase(player.getName());
        return sameId || sameName;
    }
}
