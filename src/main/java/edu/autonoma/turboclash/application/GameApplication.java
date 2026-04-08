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

        String hostIp = JOptionPane.showInputDialog("Ingrese la IP del host:");
        String hostPortInput = JOptionPane.showInputDialog(
                "Ingrese el puerto del host:",
                String.valueOf(config.getMinPort())
        );

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();

        GameContext context = bootstrap.init(puerto, playerName);

        if (hostIp != null && !hostIp.trim().isEmpty()
                && hostPortInput != null && !hostPortInput.trim().isEmpty()) {
            int hostPort = Integer.parseInt(hostPortInput.trim());

            if (!config.isValidPort(hostPort)) {
                throw new IllegalArgumentException(
                        "Invalid host port. Use between "
                                + config.getMinPort() + " and " + config.getMaxPort()
                );
            }

            context.getNetwork().getPeer().agregarPeer(hostIp.trim(), hostPort);
            context.getNetwork().discover(hostIp.trim(), hostPort);
            context.getNetwork().join(context, context.getLocalPlayer());
        }

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
        String puertoProperty = System.getProperty("puerto");
        String puertoInput = puertoProperty;

        if (puertoInput == null || puertoInput.trim().isEmpty()) {
            puertoInput = JOptionPane.showInputDialog(
                    "Ingrese el puerto local:",
                    String.valueOf(config.getMinPort())
            );
        }

        if (puertoInput == null || puertoInput.trim().isEmpty()) {
            throw new IllegalArgumentException("Local port is required");
        }

        int puerto = Integer.parseInt(puertoInput.trim());

        if (!config.isValidPort(puerto)) {
            throw new IllegalArgumentException(
                    "Invalid port. Use between "
                            + config.getMinPort() + " and " + config.getMaxPort()
            );
        }

        return puerto;
    }
}
