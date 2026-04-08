package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameConstants;

import edu.autonoma.turboclash.domain.services.GameRulesManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;
    private Runnable onCountdownFinished;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<Integer, JLabel> healthLabels = new HashMap<>();

    private boolean gameFinished = false;

    private final GameRulesManager gameRulesManager;
    private final GameResultManager gameResultManager;
    private final List<Player> players = new ArrayList<>();

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

        gameRulesManager = new GameRulesManager(GameConstants.DEFAULT_TARGET_SCORE);
        gameResultManager = new edu.autonoma.turboclash.logic.GameResultManager();

        initializeCountdownUI();
    }

    public void setBackgroundPanel(FondoAnimadoPanel fondoAnimadoPanel) {
        this.fondoAnimadoPanel = fondoAnimadoPanel;
    }

    public void setPlayers(List<Player> players) {
        this.players.clear();
        if (players != null) {
            this.players.addAll(players);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void updateScore(int points) {
        Puntaje.setText("Puntaje: " + points);
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

        updateHealth(car.getLives(), car);
        checkFinishForCar(car);
    }

    public void updateCars(List<Player> players) {
        if (players == null) return;

        setPlayers(players);

        for (Player p : players) {
            if (p != null && p.getCar() != null) {
                updateCarPosition(p.getCar());
            }
        }

        checkGameEnd();
    }

    public void updateHealth(int lives, Car car) {
        for (int i = 0; i < 3; i++) {
            final int heartIndex = i;

            JLabel heart = healthLabels.computeIfAbsent(heartIndex, id -> {
                JLabel lbl = new JLabel(getIcon("/image/Health.png", 25, 25));
                panel1.add(lbl);
                return lbl;
            });

            heart.setBounds((int) car.getX() + (i * 30), (int) car.getY() - 30, 25, 25);
            heart.setVisible(i < lives && car.isActive());
        }
    }

    public void updateObstacles(List<Obstacle> obstacles) {
        obstacleLabels.values().forEach(l -> l.setVisible(false));

        if (obstacles == null) return;

        for (Obstacle obs : obstacles) {
            JLabel lbl = obstacleLabels.computeIfAbsent(obs.getId(), id -> {
                String path;
                String typeStr = obs.getType().toString();

                if ("OIL".equals(typeStr)) {
                    path = "/image/Oil_Spill.png";
                } else if ("BARRIER".equals(typeStr)) {
                    path = "/image/Barrier.png";
                } else {
                    path = "/image/Cone.png";
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
        itemLabels.values().forEach(l -> l.setVisible(false));

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

    public void setOnCountdownFinished(Runnable action) {
        this.onCountdownFinished = action;
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

        javax.swing.Timer timer = new javax.swing.Timer(1000, null);
        timer.addActionListener(e -> {
            seconds[0]--;

            if (seconds[0] > 0) {
                countdownLabel.setText(String.valueOf(seconds[0]));
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

    public void requestGameFocus() {
        panel1.requestFocusInWindow();
    }

    public JPanel getPanel() {
        return panel1;
    }

    public GameRulesManager getGameRulesManager() {
        return gameRulesManager;
    }

    private JLabel createScoreLabel() {
        JLabel label = new JLabel("Puntaje: 0");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBounds(20, 40, 250, 40);
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

    private void checkFinishForCar(Car car) {
        if (car == null || fondoAnimadoPanel == null || !fondoAnimadoPanel.isMetaVisible()) return;

        for (Player player : players) {
            if (player != null && player.getCar() == car && !player.isFinishReached()) {
                if ((int) car.getX() + 100 >= fondoAnimadoPanel.getMetaX()) {
                    gameRulesManager.applyFinishBonus(player);
                    checkGameEnd();
                }
                break;
            }
        }
    }

    private void checkGameEnd() {
        if (gameFinished) return;
        if (players.isEmpty() || fondoAnimadoPanel == null || fondoAnimadoPanel.isJuegoTerminado()) {
            return;
        }

        int aliveCount = 0;
        for (Player player : players) {
            if (player != null && player.isAlive()) {
                aliveCount++;
            }
        }

        boolean someoneReachedFinish = false;
        for (Player player : players) {
            if (player != null && player.isFinishReached()) {
                someoneReachedFinish = true;
                break;
            }
        }

        if (someoneReachedFinish || aliveCount <= 1) {
            finishGame();
        }
    }

    private void finishGame() {
        gameFinished = true;

        List<Player> ranking = gameResultManager.calculateRanking(players);

        if (fondoAnimadoPanel != null) {
            fondoAnimadoPanel.terminarJuego(ranking);
            return;
        }

        String primero = !ranking.isEmpty() ? ranking.get(0).getName() : "";
        String segundo = ranking.size() > 1 ? ranking.get(1).getName() : "";
        String tercero = ranking.size() > 2 ? ranking.get(2).getName() : "";
        String cuarto = ranking.size() > 3 ? ranking.get(3).getName() : "";

        SwingUtilities.invokeLater(() ->
                new EndGameWindowFrame(primero, segundo, tercero, cuarto)
        );
    }
}