package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private final Map<String, JLabel> heartLabels = new HashMap<>();
    private final Map<String, JLabel> nameLabels = new HashMap<>();

    private FondoAnimadoPanel fondoAnimadoPanel;

    /**
     * Tamaño base de diseño.
     */
    private static final int BASE_WIDTH = 1000;
    private static final int BASE_HEIGHT = 700;

    private static final int START_X = 80;

    /**
     * Estado visual del juego.
     * false = esperando jugadores
     * true = partida iniciada
     */
    private boolean gameStarted = false;

    /**
     * Jugador local real.
     */
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
        panel1.add(btnClose);

        btnClose.addActionListener(e -> System.exit(0));

        panel1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateResponsiveLayout();
                refreshAllSprites();
            }
        });

        SwingUtilities.invokeLater(() -> {
            updateResponsiveLayout();
            refreshAllSprites();
        });
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

    public void setOnCountdownFinished(Runnable action) {
        this.onCountdownFinished = action;
    }

    /**
     * Registra cuál es el jugador local real.
     */
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

    public void showWaitingPlayers(int connectedPlayers, int expectedPlayers) {
        gameStarted = false;
        statusLabel.setText("Esperando jugadores... " + connectedPlayers + "/" + expectedPlayers);
        statusLabel.setVisible(true);
        panel1.repaint();
    }

    public void showGameStarted() {
        gameStarted = true;
        statusLabel.setVisible(false);
        panel1.repaint();
    }

    public void updateScore(int points) {
        Puntaje.setText("Puntaje: " + points);
    }


    public void updateCars(List<Player> players) {
        if (players == null) {
            return;
        }

        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            boolean visible = gameStarted || isLocalPlayer(player);
            updateCarPosition(player, visible);
        }

        panel1.revalidate();
        panel1.repaint();
    }



    private void updateCarPosition(Player player, boolean visible) {
        if (player == null) {
            return;
        }

        Car car = player.getCar();
        if (car == null || car.getId() == null) {
            return;
        }

        int carWidth = getResponsiveCarWidth();
        int carHeight = getResponsiveCarHeight();

        JLabel lbl = carLabels.computeIfAbsent(car.getId(), id -> {
            String imagePath = normalizeCarImagePath(car.getCarImage());
            JLabel newLbl = new JLabel(getIcon(imagePath, carWidth, carHeight));
            newLbl.setOpaque(false);
            panel1.add(newLbl);
            panel1.setComponentZOrder(newLbl, 0);
            return newLbl;
        });

        String imagePath = normalizeCarImagePath(car.getCarImage());
        lbl.setIcon(getIcon(imagePath, carWidth, carHeight));

        int drawX = Math.max(0, scaleX((int) car.getX()));
        int drawY = Math.max(0, scaleY((int) car.getY()));

        lbl.setBounds(drawX, drawY, carWidth, carHeight);
        lbl.setVisible(visible && car.isActive());

        updateHealthVisual(drawX, drawY, car.getId(), visible && car.isActive(), car.getLives());
        updateNameVisual(drawX, drawY, player.getName(), car.getId(), visible && car.isActive());
    }




    private void updateHealthVisual(int x, int y, String carId, boolean active, int lives) {
        int heartSize = Math.max(14, scale(20));
        int spacing = Math.max(16, scale(22));
        int offsetY = Math.max(10, scale(28));

        for (int i = 0; i < 3; i++) {
            String heartKey = carId + "_heart_" + i;

            JLabel heart = heartLabels.computeIfAbsent(heartKey, id -> {
                JLabel lbl = new JLabel();
                panel1.add(lbl);
                return lbl;
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
            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setForeground(Color.WHITE);
            panel1.add(lbl);
            return lbl;
        });

        nameLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
        nameLabel.setText(playerName != null && !playerName.isBlank() ? playerName : "Jugador");
        nameLabel.setBounds(x - scale(10), y - offsetY, width, height);
        nameLabel.setVisible(active);
    }

    public void updateObstacles(List<Obstacle> obstacles) {
        obstacleLabels.values().forEach(lbl -> lbl.setVisible(false));

        if (obstacles == null) {
            return;
        }

        int obstacleSize = Math.max(28, scale(45));

        for (Obstacle obs : obstacles) {
            JLabel lbl = obstacleLabels.computeIfAbsent(obs.getId(), id -> {
                String path;
                switch (obs.getType()) {
                    case OIL -> path = "/image/Oil_Spill.png";
                    case BARRIER -> path = "/image/Barrier.png";
                    default -> path = "/image/Cone.png";
                }
                JLabel newLbl = new JLabel(getIcon(path, obstacleSize, obstacleSize));
                panel1.add(newLbl);
                return newLbl;
            });

            String path;
            switch (obs.getType()) {
                case OIL -> path = "/image/Oil_Spill.png";
                case BARRIER -> path = "/image/Barrier.png";
                default -> path = "/image/Cone.png";
            }

            lbl.setIcon(getIcon(path, obstacleSize, obstacleSize));
            lbl.setBounds(scaleX((int) obs.getX()), scaleY((int) obs.getY()), obstacleSize, obstacleSize);
            lbl.setVisible(obs.isVisible());
        }
    }

    public void updateItems(List<Item> items) {
        itemLabels.values().forEach(lbl -> lbl.setVisible(false));

        if (items == null) {
            return;
        }

        int itemSize = Math.max(20, scale(30));

        for (Item item : items) {
            JLabel lbl = itemLabels.computeIfAbsent(item.getId(), id -> {
                JLabel newLbl = new JLabel(getIcon("/image/Coin.png", itemSize, itemSize));
                panel1.add(newLbl);
                return newLbl;
            });

            lbl.setIcon(getIcon("/image/Coin.png", itemSize, itemSize));
            lbl.setBounds(scaleX((int) item.getX()), scaleY((int) item.getY()), itemSize, itemSize);
            lbl.setVisible(item.isVisible());
        }
    }

    /**
     * Organiza la salida inicial sin pisar posiciones sincronizadas por red.
     */
    public void prepareRaceStart(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return;
        }

        int[] lanes = getResponsiveLanesYBase();

        for (Player player : players) {
            if (player == null || player.getCar() == null) {
                continue;
            }

            Car car = player.getCar();

            boolean uninitializedX = car.getX() <= 0;
            boolean uninitializedY = car.getY() <= 0;
            boolean stillAtStartX = Math.abs(car.getX() - START_X) < 5;

            if (uninitializedX) {
                car.setPosition(START_X, getStartLaneY(player, lanes));
            } else if (uninitializedY) {
                car.setPosition(car.getX(), getStartLaneY(player, lanes));
            } else if (stillAtStartX && !isValidLane((int) car.getY(), lanes)) {
                car.setPosition(START_X, getStartLaneY(player, lanes));
            }
        }

        panel1.repaint();
    }

    private boolean isValidLane(int y, int[] lanes) {
        for (int lane : lanes) {
            if (Math.abs(lane - y) < 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Carriles en coordenadas base del juego.
     * Aquí puedes moverlos fino sin romper el responsive.
     */
    private int[] getResponsiveLanesYBase() {
        return new int[] {
                140,
                280,
                420,
                560
        };
    }

    private int getStartLaneY(Player player, int[] lanes) {
        if (player == null || player.getId() == null) {
            return lanes[0];
        }

        String id = player.getId().trim();

        switch (id) {
            case "5001":
            case "player1":
            case "jugador1":
                return lanes[0];

            case "5002":
            case "player2":
            case "jugador2":
                return lanes[1];

            case "5003":
            case "player3":
            case "jugador3":
                return lanes[2];

            case "5004":
            case "player4":
            case "jugador4":
                return lanes[3];

            default:
                return lanes[Math.abs(id.hashCode()) % lanes.length];
        }
    }

    private boolean isLocalPlayer(Player player) {
        if (player == null) {
            return false;
        }

        boolean sameId =
                localPlayerId != null
                        && player.getId() != null
                        && localPlayerId.equals(player.getId());

        boolean sameName =
                localPlayerName != null
                        && player.getName() != null
                        && localPlayerName.equalsIgnoreCase(player.getName());

        return sameId || sameName;
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
        int panelWidth = panel1.getWidth();
        int panelHeight = panel1.getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
            panelWidth = BASE_WIDTH;
            panelHeight = BASE_HEIGHT;
        }

        int btnSize = scale(55);
        int marginRight = scale(15);
        int topMargin = scale(20);

        btnClose.setBounds(
                panelWidth - btnSize - marginRight,
                topMargin,
                btnSize,
                btnSize
        );
        btnClose.setFont(new Font("Arial", Font.BOLD, Math.max(12, scale(18))));

        Puntaje.setBounds(
                scaleX(20),
                scaleY(40),
                Math.max(180, scaleX(260)),
                Math.max(25, scaleY(40))
        );
        Puntaje.setFont(new Font("Arial", Font.BOLD, Math.max(14, scale(24))));

        int statusWidth = Math.max(260, scale(500));
        int statusHeight = Math.max(30, scale(40));

        statusLabel.setBounds(
                panelWidth / 2 - statusWidth / 2,
                panelHeight / 2 - statusHeight / 2,
                statusWidth,
                statusHeight
        );
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(0, 0, 0, 150));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, Math.max(14, scale(22))));

        countdownLabel.setBounds(
                panelWidth / 2 - scale(100),
                panelHeight / 2 - scale(40),
                Math.max(120, scale(200)),
                Math.max(50, scale(80))
        );
        countdownLabel.setFont(new Font("Arial", Font.BOLD, Math.max(24, scale(48))));
    }

    private void refreshAllSprites() {
        for (Map.Entry<String, JLabel> entry : carLabels.entrySet()) {
            JLabel lbl = entry.getValue();
            Rectangle bounds = lbl.getBounds();

            if (bounds.width <= 0 || bounds.height <= 0) {
                continue;
            }

            String key = entry.getKey();
            JLabel name = nameLabels.get(key);
            if (name != null && name.isVisible()) {
                name.setFont(new Font("Arial", Font.BOLD, Math.max(10, scale(14))));
            }
        }

        panel1.revalidate();
        panel1.repaint();
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
        double factor = Math.min(scaleX, scaleY);

        return Math.max(1, (int) Math.round(baseValue * factor));
    }

    private int scaleX(int baseValue) {
        int currentWidth = panel1.getWidth() > 0 ? panel1.getWidth() : BASE_WIDTH;
        double factor = currentWidth / (double) BASE_WIDTH;
        return (int) Math.round(baseValue * factor);
    }

    private int scaleY(int baseValue) {
        int currentHeight = panel1.getHeight() > 0 ? panel1.getHeight() : BASE_HEIGHT;
        double factor = currentHeight / (double) BASE_HEIGHT;
        return (int) Math.round(baseValue * factor);
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