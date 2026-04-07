package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import javax.swing.*;
import java.awt.*;

public class GameWindowFrame extends JFrame {

    private final GameWindow view;
    private Runnable countdownAction;

    public GameWindowFrame(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.view = new GameWindow();

        setupFrameProperties();
        setupContentLayout(keyboardInput, mouseInput);

        setVisible(true);

        view.requestGameFocus();

        if (countdownAction != null) {
            view.startCountdown();
        }
    }

    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setupContentLayout(KeyboardInput keyboardInput, MouseInput mouseInput) {

        JPanel gamePanel = view.getPanel();
        gamePanel.setOpaque(false);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        gamePanel.setPreferredSize(screen);
        gamePanel.setMinimumSize(screen);

        try {
            FondoAnimadoPanel fondo = new FondoAnimadoPanel("/image/Track.png");
            fondo.setLayout(new BorderLayout());
            fondo.setPreferredSize(screen);
            fondo.add(gamePanel, BorderLayout.CENTER);
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