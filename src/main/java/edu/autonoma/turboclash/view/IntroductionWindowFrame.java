package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;

import javax.swing.*;
import java.awt.BorderLayout;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.core.GameApplication;
import edu.autonoma.turboclash.core.GameBootstrap;
import edu.autonoma.turboclash.exception.InvalidGameStateException;
import edu.autonoma.turboclash.exception.InvalidNameException;

import edu.autonoma.turboclash.core.GameFactory;
import edu.autonoma.turboclash.core.WorldFactory;
import edu.autonoma.turboclash.core.NetworkFactory;

import edu.autonoma.turboclash.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.network.core.NetworkConfig;
import edu.autonoma.turboclash.validation.GameStateValidator;
import edu.autonoma.turboclash.validation.PlayerNameValidator;

public class IntroductionWindowFrame extends JFrame implements IntroductionWindowListener {
    private IntroductionWindow view;
    public IntroductionWindowFrame(IntroductionWindowListener listener) {

        view = new IntroductionWindow(listener);

        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Start.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);

        setTitle("Introducción");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startGame(String playerName) {

        GameConfig config = new GameConfig();

        GameFactory gameFactory = new GameFactory(config);
        WorldFactory worldFactory = new WorldFactory(config);


        NetworkConfig networkConfig = new NetworkConfig(config);
        NetworkFactory networkFactory = new NetworkFactory(networkConfig);

        GameBootstrap bootstrap = new GameBootstrap(
                gameFactory,
                worldFactory,
                networkFactory,
                config
        );

        GameApplication app = new GameApplication(bootstrap, config);
        app.start(playerName);
    }
}