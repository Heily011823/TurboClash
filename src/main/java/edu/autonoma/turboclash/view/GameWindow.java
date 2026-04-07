package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindow {

    public JPanel panel1;
    public JLabel Puntaje;

    private JLabel countdownLabel;

    private final List<JLabel> carLabels = new ArrayList<>();
    private final List<JLabel> healthLabels = new ArrayList<>();
    private final List<JLabel> obstacleLabels = new ArrayList<>();
    private final List<JLabel> itemLabels = new ArrayList<>();

    private final String[] SKINS = {
            "/image/Car_Blue.png",
            "/image/Car_Red.png",
            "/image/Car_Yellow.png",
            "/image/Car_Brown.png"
    };

    public GameWindow() {
        if (panel1 != null) {
            panel1.setLayout(null);
            panel1.setFocusable(true);
            panel1.requestFocusInWindow();
            panel1.setPreferredSize(GameViewport.size());
        }

        initializeScoreUI();
        initializeCountdownUI();
    }

    private void initializeScoreUI() {
        if (Puntaje != null) {
            Puntaje.setForeground(Color.YELLOW);
            Puntaje.setFont(new Font("Arial", Font.BOLD, 24));
            Puntaje.setText("Score: 0");
            Puntaje.setBounds(20, 43, 200, 40);
        }
    }

    private void initializeCountdownUI() {
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 48));
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setBounds((GameViewport.WIDTH - 200) / 2, (GameViewport.HEIGHT - 80) / 2, 200, 80);
        countdownLabel.setVisible(false);

        if (panel1 != null) {
            panel1.add(countdownLabel);
            panel1.setComponentZOrder(countdownLabel, 0);
        }
    }

    public JPanel getPanel() {
        return panel1;
    }

    public void requestGameFocus() {
        if (panel1 != null) {
            panel1.setFocusable(true);
            panel1.requestFocusInWindow();
        }
    }

    public void updateScore(int points) {
        if (Puntaje != null) {
            SwingUtilities.invokeLater(() -> {
                Puntaje.setText("Score: " + points);
                Puntaje.setSize(250, 40);
                Puntaje.setVisible(true);
                Puntaje.repaint();
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
            heart.setBounds((int) car.getX() + (i * 30), (int) car.getY() - 30, 25, 25);
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
                lbl.setBounds((int) item.getX(), (int) item.getY(), 30, 30);
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
            String imagePath = "/image/" + car.getCarImage();
            java.net.URL carUrl = getClass().getResource(imagePath);

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

                String imagePath = "/image/Cone.png";
                if ("OIL".equals(obs.getType())) imagePath = "/image/Oil_Spill.png";
                else if ("BARRIER".equals(obs.getType())) imagePath = "/image/Barrier.png";

                lbl.setIcon(getIcon(imagePath, 45, 45));
                lbl.setBounds((int) obs.getX(), (int) obs.getY(), 45, 45);
                lbl.setVisible(obs.isVisible());
            } else {
                lbl.setVisible(false);
            }
        }
    }

    public void prepararInicioCarrera(List<Car> cars) {
        for (int i = 0; i < cars.size() && i < GameViewport.LANE_Y.length; i++) {
            cars.get(i).setPosition(GameViewport.CAR_START_X, GameViewport.laneY(i));
        }

        updateCars(cars);

        for (Car car : cars) {
            updateHealth(car.getLives(), car);
        }
    }

    public void iniciarCuentaRegresiva(Runnable onFinish) {
        if (countdownLabel == null) return;

        final int[] segundos = {3};

        countdownLabel.setText("3");
        countdownLabel.setVisible(true);
        countdownLabel.repaint();

        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            segundos[0]--;

            if (segundos[0] > 0) {
                countdownLabel.setText(String.valueOf(segundos[0]));
            } else if (segundos[0] == 0) {
                countdownLabel.setText("GO!");
            } else {
                timer.stop();
                countdownLabel.setVisible(false);
                onFinish.run();
            }
        });

        timer.start();
    }

    private ImageIcon getIcon(String path, int w, int h) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) return new ImageIcon();
        return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    private JLabel createScalableLabel(String path, int width, int height) {
        return new JLabel(getIcon(path, width, height));
    }
}
