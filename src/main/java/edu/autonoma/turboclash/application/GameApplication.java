package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.infrastructure.network.config.PeerConfigEntry;
import edu.autonoma.turboclash.infrastructure.network.config.PeerConfigLoader;
import edu.autonoma.turboclash.presentation.view.FondoAnimadoPanel;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.GameWindowFrame;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * Representa la clase `GameApplication` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameApplication {

    private final GameBootstrap bootstrap;
    private final GameConfig config;

    /**
     * Crea una nueva instancia de `GameApplication`.
     * @param bootstrap valor del parametro `bootstrap`
     * @param config valor del parametro `config`
     */
    public GameApplication(GameBootstrap bootstrap, GameConfig config) {
        this.bootstrap = bootstrap;
        this.config = config;
    }

    /**
     * Ejecuta la operacion publica `start`.
     * @param playerName valor del parametro `playerName`
     */
    public void start(String playerName) {
        int puerto = getPuerto();

        KeyboardInput keyboard = new KeyboardInput();
        MouseInput mouse = new MouseInput();

        GameWindowFrame mainFrame = new GameWindowFrame(keyboard, mouse);
        GameWindow view = mainFrame.getGameView();

        GameContext context = bootstrap.init(puerto, playerName);
        Player localPlayer = context.getLocalPlayer();
        view.setLocalPlayer(localPlayer);

        List<PeerConfigEntry> peers = PeerConfigLoader.loadFromResource("/peers.json");
        for (PeerConfigEntry peerInfo : peers) {
            if (peerInfo.getPuerto() != puerto) {
                context.getPeer().agregarPeer(peerInfo.getIp(), peerInfo.getPuerto());
            }
        }

        context.getNetwork().connect(context, localPlayer);
        view.prepareRaceStart(context.getPlayers());
        view.updateCars(context.getPlayers());
        view.showWaitingPlayers(context.getMatch().getConnectedPlayerCount(), context.getMatch().getMinPlayers());
        view.requestGameFocus();

        GameLoop loop = new GameLoop(config.getFrameDelay());
        Thread gameThread = new Thread(() -> loop.run(context, view, keyboard, mouse, puerto));
        gameThread.setName("GameLoop-Thread");
        gameThread.setDaemon(true);
        gameThread.start();
    }

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
            throw new IllegalArgumentException("El puerto local es obligatorio");
        }

        int puerto = Integer.parseInt(puertoInput.trim());

        if (!config.isValidPort(puerto)) {
            throw new IllegalArgumentException(
                    "Puerto invalido. Use entre "
                            + config.getMinPort() + " y " + config.getMaxPort()
            );
        }

        return puerto;
    }
}
