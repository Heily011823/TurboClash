package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class GameWindow {

    public JPanel panel1;
    public JLabel scoreLabel;

    private JLabel countdownLabel;
    private JButton pauseButton;

    private Runnable onPauseRequest;
    private Runnable onCountdownFinished;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> nameLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<Integer, JLabel> healthLabels = new HashMap<>();

    public GameWindow() {

        panel1 = new JPanel(null);
        panel1.setOpaque(false);

        scoreLabel = createScoreLabel();
        panel1.add(scoreLabel);

        initCountdown();
        initPauseButton();
    }

    // =========================
    // UI CONTROL (SRP CLEAN)
    // =========================
    private void initPauseButton() {
        pauseButton = new JButton("⏸");

        pauseButton.setBounds(20, 20, 50, 50);
        pauseButton.setBackground(new Color(180, 30, 30));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFocusPainted(false);

        pauseButton.addActionListener(e -> {
            if (onPauseRequest != null) onPauseRequest.run();
        });

        panel1.add(pauseButton);
    }

    // =========================
    // SCORE
    // =========================
    public void updateScore(int score) {
        scoreLabel.setText("SCORE: " + score);
    }

    private JLabel createScoreLabel() {
        JLabel label = new JLabel("SCORE: 0");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("Consolas", Font.BOLD, 22));
        label.setBounds(20, 70, 250, 40);
        return label;
    }

    // =========================
    // PLAYER RENDERING (CORE)
    // =========================
    public void renderPlayer(Player player) {

        Car car = player.getCar();

        JLabel carLbl = carLabels.computeIfAbsent(car.getId(), id -> {
            JLabel lbl = new JLabel();
            panel1.add(lbl);
            return lbl;
        });

        carLbl.setIcon(loadIcon("/image/" + car.getCarImage(), 100, 50));
        carLbl.setBounds((int) car.getX(), (int) car.getY(), 100, 50);
        carLbl.setVisible(car.isActive());

        JLabel nameLbl = nameLabels.computeIfAbsent(car.getId(), id -> {
            JLabel lbl = new JLabel();
            lbl.setFont(new Font("Consolas", Font.BOLD, 14));
            lbl.setForeground(Color.WHITE);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0, 0, 0, 160));
            panel1.add(lbl);
            return lbl;
        });

        nameLbl.setText(player.getName());
        nameLbl.setBounds((int) car.getX(), (int) car.getY() - 20, 120, 18);
        nameLbl.setVisible(car.isActive());

        renderHealth(car);
    }

    public void renderPlayers(List<Player> players) {
        if (players == null) return;
        players.forEach(this::renderPlayer);
    }

    // =========================
    // HEALTH
    // =========================
    private void renderHealth(Car car) {

        for (int i = 0; i < 3; i++) {

            JLabel heart = healthLabels.computeIfAbsent(i, id -> {
                JLabel lbl = new JLabel(loadIcon("/image/Health.png", 22, 22));
                panel1.add(lbl);
                return lbl;
            });

            heart.setBounds((int) car.getX() + (i * 25), (int) car.getY() - 35, 22, 22);
            heart.setVisible(i < car.getLives());
        }
    }

    // =========================
    // COUNTDOWN (UI ONLY)
    // =========================
    public void startCountdown() {

        final int[] time = {3};

        countdownLabel.setVisible(true);

        Timer timer = new Timer(1000, e -> {

            time[0]--;

            if (time[0] > 0) {
                countdownLabel.setText(String.valueOf(time[0]));
            } else if (time[0] == 0) {
                countdownLabel.setText("GO!");
            } else {
                ((Timer) e.getSource()).stop();
                countdownLabel.setVisible(false);

                if (onCountdownFinished != null)
                    onCountdownFinished.run();
            }
        });

        timer.start();
    }

    private void initCountdown() {
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Consolas", Font.BOLD, 50));
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBounds(400, 250, 200, 80);
        countdownLabel.setVisible(false);
        panel1.add(countdownLabel);
    }

    // =========================
    // PUBLIC API
    // =========================
    public JPanel getPanel() {
        return panel1;
    }

    public void requestFocusGame() {
        panel1.requestFocusInWindow();
    }

    public void setOnPauseRequest(Runnable r) {
        this.onPauseRequest = r;
    }

    public void setOnCountdownFinished(Runnable r) {
        this.onCountdownFinished = r;
    }

    // =========================
    // UTIL
    // =========================
    private ImageIcon loadIcon(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return new ImageIcon();

        return new ImageIcon(
                new ImageIcon(url).getImage()
                        .getScaledInstance(w, h, Image.SCALE_SMOOTH)
        );
    }
}