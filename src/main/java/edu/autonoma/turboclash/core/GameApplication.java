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

    public void start(String nombreJugador) {
        int puerto = getPuerto();

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameContext context = bootstrap.init(puerto, nombreJugador);
        GameLoop loop = new GameLoop(config.getFrameDelay());

        GameWindowFrame frame = new GameWindowFrame(keyboard, mouse, null);

        Runnable startGame = () -> new Thread(() ->
                loop.run(context, frame.getView(), keyboard, mouse, puerto)
        ).start();

        frame.setCountdownAction(startGame);

        frame.getView().requestGameFocus();
    }
    private int getPuerto() {
        int puerto = Integer.parseInt(
                System.getProperty("puerto", String.valueOf(config.getMinPort()))
        );

        if (!config.isValidPort(puerto)) {
            throw new IllegalArgumentException(
                    "Puerto inválido. Usa entre "
                            + config.getMinPort() + " y " + config.getMaxPort()
            );
        }

        return puerto;
    }
}