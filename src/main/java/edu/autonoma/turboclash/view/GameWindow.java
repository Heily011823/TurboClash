package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.Car;
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Obstacle;

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

    private final List<JLabel> carLabels = new ArrayList<>();
    private final List<JLabel> nameLabels = new ArrayList<>();
    private final List<JLabel> obstacleLabels = new ArrayList<>();
    private final List<JLabel> itemLabels = new ArrayList<>();

    private final Map<String, List<JLabel>> healthLabelsByCar = new HashMap<>();

    public GameWindow() {

        if (panel1 != null) {
            panel1.setLayout(null);
            panel1.setPreferredSize(new Dimension(1000, 700));
            panel1.setMinimumSize(new Dimension(1000, 700));
            panel1.setFocusable(true);
            panel1.requestFocusInWindow();
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
        countdownLabel.setBounds(300, 200, 200, 80);
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

    public void updateCars(List<Car> cars) {
        if (panel1 == null) return;

        while (carLabels.size() < cars.size()) {
            JLabel carLabel = new JLabel();
            carLabels.add(carLabel);
            panel1.add(carLabel);

            JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabels.add(nameLabel);
            panel1.add(nameLabel);
        }

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            JLabel carLabel = carLabels.get(i);
            JLabel nameLabel = nameLabels.get(i);

            String imagePath = "/image/" + car.getCarImage();
            java.net.URL carUrl = getClass().getResource(imagePath);

            if (carUrl != null) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(carUrl).getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)
                );
                carLabel.setIcon(icon);
            }

            int x = (int) car.getX();
            int y = (int) car.getY();

            carLabel.setBounds(x, y, 100, 50);
            carLabel.setVisible(car.isActive());

            nameLabel.setText(car.getId());
            nameLabel.setBounds(x - 10, y - 58, 120, 18);
            nameLabel.setVisible(car.isActive());

            updateHealth(car.getLives(), car);
        }

        for (JLabel nameLabel : nameLabels) {
            panel1.setComponentZOrder(nameLabel, 0);
        }



        for (List<JLabel> hearts : healthLabelsByCar.values()) {
            for (JLabel heart : hearts) {
                panel1.setComponentZOrder(heart, 0);
            }
        }

        for (JLabel carLabel : carLabels) {
            panel1.setComponentZOrder(carLabel, 1);
        }

        if (countdownLabel != null) {
            panel1.setComponentZOrder(countdownLabel, 0);
        }

        panel1.repaint();
    }

    public void updateHealth(int lives, Car car) {
        if (car == null || panel1 == null) return;

        String carId = car.getId();

        List<JLabel> hearts = healthLabelsByCar.get(carId);
        if (hearts == null) {
            hearts = new ArrayList<>();

            java.net.URL imgUrl = getClass().getResource("/image/Health.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(imgUrl).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)
                );

                for (int i = 0; i < 3; i++) {
                    JLabel heart = new JLabel(icon);
                    hearts.add(heart);
                    panel1.add(heart);
                }
            }

            healthLabelsByCar.put(carId, hearts);
        }

        int x = (int) car.getX();
        int y = (int) car.getY();

        int heartsStartX = x + 10;
        int heartsY = y - 30;

        for (int i = 0; i < hearts.size(); i++) {
            JLabel heart = hearts.get(i);
            heart.setBounds(heartsStartX + (i * 26), heartsY, 24, 24);
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
        int startX = 80;
        int[] lanesY = {100, 190, 280, 370};

        for (int i = 0; i < cars.size() && i < lanesY.length; i++) {
            cars.get(i).setPosition(startX, lanesY[i]);
        }

        updateCars(cars);
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