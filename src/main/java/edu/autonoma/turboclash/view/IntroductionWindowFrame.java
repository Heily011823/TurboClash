package edu.autonoma.turboclash.view;

import javax.swing.*;

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
import edu.autonoma.turboclash.sound.SoundManager;
import edu.autonoma.turboclash.validation.GameStateValidator;
import edu.autonoma.turboclash.validation.PlayerNameValidator;

public class IntroductionWindowFrame extends JFrame implements IntroductionWindowListener {

    public IntroductionWindowFrame() {
        IntroductionWindow view = new IntroductionWindow(this, SoundManager.getInstance());
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
                config,
                SoundManager.getInstance()
        );

        GameApplication app = new GameApplication(bootstrap, config);
        app.start(playerName);
    }
}