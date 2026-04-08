package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.GameWindowFrame;

/**
 * Representa la responsabilidad de {@code GameApplication} en la capa de aplicacion.
 */
import javax.swing.JOptionPane;

public class GameApplication {

    private final GameBootstrap bootstrap;
    private final GameConfig config;

    /**
     * Crea una nueva instancia de {@code GameApplication}.
     *
     * @param bootstrap valor del parametro {@code bootstrap}
     * @param config valor del parametro {@code config}
     */
    public GameApplication(GameBootstrap bootstrap, GameConfig config) {
        this.bootstrap = bootstrap;
        this.config = config;
    }

    /**
     * Inicia la operacion principal del metodo.
     *
     * @param playerName valor del parametro {@code playerName}
     */
    public void start(String playerName) {

        int puerto = getPuerto();

        // 🔥 NUEVO: pedir IP del host (Radmin)
        String hostIp = JOptionPane.showInputDialog("Ingrese la IP del host:");

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();

        GameContext context = bootstrap.init(puerto, playerName);


        context.getNetwork().getPeer().agregarPeer(hostIp, puerto);

        context.getNetwork().join(context, context.getLocalPlayer());

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



    /**
     * Obtiene el valor de {@code Puerto}.
     *
     * @return valor de {@code Puerto}
     */
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
