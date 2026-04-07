package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.input.*;
import javax.swing.*;
import java.awt.*;

public class GameWindowFrame extends JFrame {

    private final GameWindow gameView;

    public GameWindowFrame(KeyboardInput keyboard, MouseInput mouse) {
        this.gameView = new GameWindow();

        setupFrameProperties();
        setupContentLayout();
        bindInputs(keyboard, mouse);

        setVisible(true);
        gameView.getPanel().requestFocusInWindow();
    }

    private void setupFrameProperties() {
        setTitle("TurboClash - Racing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setupContentLayout() {
        // FondoAnimadoPanel es el decorador visual (Open/Closed Principle)
        FondoAnimadoPanel background = new FondoAnimadoPanel("/image/Track.png");
        background.setLayout(new BorderLayout());

        // Agregamos la capa de juego (transparente) sobre el fondo
        background.add(gameView.getPanel(), BorderLayout.CENTER);

        setContentPane(background);
        pack();
        setLocationRelativeTo(null);
    }

    private void bindInputs(KeyboardInput k, MouseInput m) {
        new GameInputBinder(k, m).bind(gameView.getPanel());
    }

    public GameWindow getGameView() { return gameView; }
}