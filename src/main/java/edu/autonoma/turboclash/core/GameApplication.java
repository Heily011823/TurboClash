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

        GameWindowFrame frame = new GameWindowFrame(keyboard, mouse);
        GameWindow window = frame.getView();

        GameContext context = bootstrap.init(puerto, nombreJugador);

        GameLoop loop = new GameLoop(config.getFrameDelay());


        new Thread(() -> {
            loop.run(context, window, keyboard, mouse, puerto);
        }).start();


        window.requestGameFocus();
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