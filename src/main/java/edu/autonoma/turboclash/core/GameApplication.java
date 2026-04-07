package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.view.*;

public class GameApplication {

    private final GameBootstrap bootstrap;

    public GameApplication(GameBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void start(String nombreJugador) {

        int puerto = getPuerto();

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame frame = new GameWindowFrame(keyboard, mouse);
        GameWindow window = frame.getView();

        GameContext context = bootstrap.init(puerto, nombreJugador);

        GameLoop loop = new GameLoop(GameConfig.getFrameDelay());
        loop.run(context, window, keyboard, mouse, puerto);
    }

    private int getPuerto() {

        int puerto = Integer.parseInt(
                System.getProperty("puerto", String.valueOf(GameConfig.MIN_PORT))
        );

        if (!GameConfig.isValidPort(puerto)) {
            throw new IllegalArgumentException(
                    "Puerto inválido. Usa entre "
                            + GameConfig.MIN_PORT + " y " + GameConfig.MAX_PORT
            );
        }

        return puerto;
    }
}