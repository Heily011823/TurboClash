package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.view.*;

public class GameApplication {

    private final GameBootstrap bootstrap;
    private final GameConfig config;

    public GameApplication(GameBootstrap bootstrap, GameConfig config) {
        this.bootstrap = bootstrap;
        this.config = config;
    }

    public void start(String playerName) {
        int puerto = getPuerto();

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();

        GameContext context = bootstrap.init(puerto, playerName);



        view.updateCars(context.getPlayers());

        GameLoop loop = new GameLoop(config.getFrameDelay());

        Runnable startGame = () -> {
            Thread gameThread = new Thread(() ->
                    loop.run(context, view, keyboard, mouse, puerto)
            );
            gameThread.setName("GameLoop-Thread");
            gameThread.start();
        };

        view.setOnCountdownFinished(startGame);
        view.requestGameFocus();


        view.startCountdown();
    }


    private int getPuerto() {
        int puerto = Integer.parseInt(
                System.getProperty("puerto", String.valueOf(config.getMinPort()))
        );

        if (!config.isValidPort(puerto)) {
            throw new IllegalArgumentException(
                    "Invalid port. Use between "
                            + config.getMinPort() + " and " + config.getMaxPort()
            );
        }

        return puerto;
    }
}