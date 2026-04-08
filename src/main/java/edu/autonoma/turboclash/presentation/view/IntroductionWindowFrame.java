package edu.autonoma.turboclash.presentation.view;

import edu.autonoma.turboclash.presentation.navigation.IntroductionWindowListener;
import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.application.GameApplication;
import edu.autonoma.turboclash.application.GameBootstrap;
import edu.autonoma.turboclash.exception.InvalidGameStateException;
import edu.autonoma.turboclash.exception.InvalidNameException;
import edu.autonoma.turboclash.infrastructure.GameFactory;
import edu.autonoma.turboclash.infrastructure.WorldFactory;
import edu.autonoma.turboclash.infrastructure.NetworkFactory;
import edu.autonoma.turboclash.infrastructure.network.core.NetworkConfig;
import edu.autonoma.turboclash.infrastructure.sound.SoundManager;
import edu.autonoma.turboclash.domain.rules.GameStateValidator;
import edu.autonoma.turboclash.domain.rules.PlayerNameValidator;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindowFrame extends JFrame implements IntroductionWindowListener {

    private IntroductionWindow view;

    public IntroductionWindowFrame() {
        this.view = new IntroductionWindow(this, SoundManager.getInstance());
        setupFrame();
    }

    private void setupFrame() {

        view.panel1.setOpaque(false);

        FondoPanel fondo = new FondoPanel("/image/Start.png");
        fondo.setLayout(new BorderLayout());
        fondo.add(view.panel1, BorderLayout.CENTER);

        setContentPane(fondo);
        setTitle("TurboClash");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ESC para salir
        fondo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ESCAPE"), "exit");

        fondo.getActionMap().put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.exit(0);
            }
        });

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