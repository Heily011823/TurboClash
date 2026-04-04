package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.view.*;

public class GameApplication {

    public void start() {

        int puerto = Integer.parseInt(System.getProperty("puerto", "5000"));

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame frame = new GameWindowFrame(keyboard, mouse);
        GameWindow window = frame.getView();

        GameBootstrap bootstrap = new GameBootstrap();
        GameContext context = bootstrap.init(puerto);

        GameLoop loop = new GameLoop();
        loop.run(context, window, keyboard, mouse, puerto);
    }
}