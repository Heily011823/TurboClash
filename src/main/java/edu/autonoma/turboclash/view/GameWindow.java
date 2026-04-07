package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;
    private Runnable onCountdownFinished;

    private final Map<String, JLabel> carLabels = new HashMap<>();
    private final Map<String, JLabel> obstacleLabels = new HashMap<>();
    private final Map<String, JLabel> itemLabels = new HashMap<>();
    private final Map<Integer, JLabel> healthLabels = new HashMap<>();

    public GameWindow() {
        if (panel1 == null) {
            panel1 = new JPanel(null);
        }

        panel1.setLayout(null);
        panel1.setOpaque(false);
        panel1.setPreferredSize(new Dimension(1000, 700));


        Puntaje = createScoreLabel();
        panel1.add(Puntaje);

        initializeCountdownUI();
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


    public void updateScore(int points) {
        Puntaje.setText("Puntaje: " + points);
    }

    public void updateCarPosition(Car car) {
        JLabel lbl = carLabels.computeIfAbsent(car.getId(), id -> {
            JLabel newLbl = new JLabel(getIcon("/image/" + car.getCarImage(), 100, 50));
            panel1.add(newLbl);
            return newLbl;
        });

        int width = car.isDebuffed() ? 90 : 100;
        int height = car.isDebuffed() ? 45 : 50;

        lbl.setBounds((int) car.getX(), (int) car.getY(), width, height);
        lbl.setVisible(car.isActive());

        if (car.isDebuffed()) {
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0, 0, 0, 80));
        } else {
            lbl.setOpaque(false);
            lbl.setBackground(null);
        }

        updateHealth(car.getLives(), car);
    }

    public void updateCars(List<Player> players) {
        if (players == null) return;
        players.forEach(p -> updateCarPosition(p.getCar()));
    }

    public void updateHealth(int lives, Car car) {
        for (int i = 0; i < 3; i++) {
            JLabel heart = healthLabels.computeIfAbsent(i, id -> {
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

        for (Obstacle obs : obstacles) {
            JLabel lbl = obstacleLabels.computeIfAbsent(obs.getId(), id -> {
                String path = "/image/" + obs.getType().getImage();

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

    public void startCountdown() {
        final int[] seconds = {3};

        countdownLabel.setText("3");
        countdownLabel.setVisible(true);

        Timer timer = new Timer(1000, null);
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

    public JPanel getPanel() {
        return panel1;
    }

    private ImageIcon getIcon(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return new ImageIcon();
        return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}