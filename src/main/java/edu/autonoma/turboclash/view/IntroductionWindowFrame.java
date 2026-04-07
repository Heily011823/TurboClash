package edu.autonoma.turboclash.view;

import javax.swing.*;

import edu.autonoma.turboclash.core.GameApplication;
import edu.autonoma.turboclash.core.GameBootstrap;
import edu.autonoma.turboclash.exception.InvalidGameStateException;
import edu.autonoma.turboclash.exception.InvalidNameException;
import edu.autonoma.turboclash.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.validation.GameStateValidator;
import edu.autonoma.turboclash.validation.PlayerNameValidator;

public class IntroductionWindowFrame extends JFrame implements IntroductionWindowListener {

    public IntroductionWindowFrame() {

        IntroductionWindow view = new IntroductionWindow(this);
        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Start.png");
        fondo.setLayout(new java.awt.BorderLayout());
        fondo.add(view.panel1);

        setContentPane(fondo);
        setTitle("Introducción");
        setSize(800, 600);
        setLocationRelativeTo(null);

        setResizable(false);
        setVisible(true);
    }

    @Override
    public void onContinuePressed(String playerName) {
        try {

            GameStateValidator.validateStart(!playerName.isBlank());
            PlayerNameValidator.validate(playerName);

            dispose();

            startGame(playerName);

        } catch (InvalidNameException | InvalidGameStateException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void startGame(String playerName) {

        new Thread(() -> {
            GameBootstrap bootstrap = new GameBootstrap();
            GameApplication app = new GameApplication(bootstrap);
            app.start(playerName);
        }).start();
    }
}