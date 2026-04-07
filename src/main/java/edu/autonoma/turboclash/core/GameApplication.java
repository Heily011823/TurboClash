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
        // 1. Obtener puerto y configurar inputs
        int puerto = getPuerto();
        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        // 2. Inicializar Ventana y Vista
        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();

        // 3. Inicializar Contexto de Juego
        GameContext context = bootstrap.init(puerto, playerName);

        // 4. Preparar estado inicial de la carrera
        view.prepararInicioCarrera(context.getCars());

        // 5. Definir la lógica de inicio tras la cuenta regresiva
        GameLoop loop = new GameLoop(config.getFrameDelay());

        Runnable startGame = () -> {
            Thread gameThread = new Thread(() ->
                    loop.run(context, view, keyboard, mouse, puerto)
            );
            gameThread.setName("GameLoop-Thread");
            gameThread.start();
        };

        // 6. Vincular inicio a la vista y solicitar foco
        view.setOnCountdownFinished(startGame);
        view.requestGameFocus();

        // Iniciar la cuenta regresiva visual
        view.iniciarCuentaRegresiva();
    }

    /**
     * SRP: Responsable de validar y obtener el puerto de red.
     */
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