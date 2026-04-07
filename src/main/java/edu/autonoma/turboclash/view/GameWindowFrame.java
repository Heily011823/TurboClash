package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.GameInputBinder;
import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;

import javax.swing.*;
import java.awt.*;

public class GameWindowFrame extends JFrame {

    private final GameWindow view;
    private Runnable countdownAction;

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.view = new GameWindow();

        setupFrameProperties();
        setupContentLayout(keyboardInput, mouseInput);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        view.requestGameFocus();

        if (countdownAction != null) {
            view.startCountdown();
        }
    }

    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
    }

    private void setupContentLayout(KeyboardInput keyboardInput, MouseInput mouseInput) {
        JPanel gamePanel = view.getPanel();
        gamePanel.setPreferredSize(new Dimension(1000, 700));
        gamePanel.setMinimumSize(new Dimension(1000, 700));
        gamePanel.setOpaque(false);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(new Dimension(1000, 700));
            fondo.add(gamePanel, BorderLayout.CENTER);

            view.setBackgroundPanel(fondo);

            setContentPane(fondo);
        } catch (Exception e) {
            gamePanel.setOpaque(true);
            gamePanel.setBackground(Color.GRAY);
            setContentPane(gamePanel);
            System.err.println("No se pudo cargar el fondo animado: " + e.getMessage());
        }

        new GameInputBinder(keyboardInput, mouseInput).bind(gamePanel);
    }

    public void setCountdownAction(Runnable countdownAction) {
        this.countdownAction = countdownAction;
        view.startCountdown();
    }

    public GameWindow getView() {
        return view;
    }

    public GameWindow getGameView() {
        return view;
    }
}