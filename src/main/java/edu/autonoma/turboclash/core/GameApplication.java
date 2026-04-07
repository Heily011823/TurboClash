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

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();


        int port = config.getMinPort();
        GameContext context = bootstrap.init(port, playerName);


        view.prepararInicioCarrera(context.getCars());


        launchGameLoop(context, view, keyboard, mouse, port);
    }

    private void launchGameLoop(GameContext ctx, GameWindow view, KeyboardInput k, MouseInput m, int port) {
        GameLoop loop = new GameLoop(config.getFrameDelay());
        Thread gameThread = new Thread(() -> loop.run(ctx, view, k, m, port));
        gameThread.setName("GameLoop-Thread");
        gameThread.start();
    }
}