package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.config.GameConfig;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.infrastructure.network.config.PeerConfigEntry;
import edu.autonoma.turboclash.infrastructure.network.config.PeerConfigLoader;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.GameWindowFrame;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Aplicación principal del juego.
 */
public class GameApplication {

    /**
     * Si juegan 4 en total, cada cliente debe ver 3 remotos.
     */
    private static final int EXPECTED_REMOTE_PLAYERS = 3;

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
        Player localPlayer = context.getLocalPlayer();


        List<PeerConfigEntry> peers = PeerConfigLoader.loadFromResource("/peers.json");

        for (PeerConfigEntry peerInfo : peers) {
            if (peerInfo.getPuerto() != puerto) {
                context.getPeer().agregarPeer(peerInfo.getIp(), peerInfo.getPuerto());
            }
        }

        System.out.println("Conectando a peers...");
        context.getNetwork().connect(context, localPlayer);

        view.updateCars(context.getPlayers());
        view.showWaitingPlayers();
        view.requestGameFocus();

        GameLoop loop = new GameLoop(config.getFrameDelay());
        AtomicBoolean gameStarted = new AtomicBoolean(false);

        Runnable startGame = () -> {
            if (!gameStarted.compareAndSet(false, true)) {
                return;
            }

            Thread gameThread = new Thread(() -> loop.run(context, view, keyboard, mouse, puerto));
            gameThread.setName("GameLoop-Thread");
            gameThread.setDaemon(true);
            gameThread.start();
        };

        new Thread(() -> {
            long timeout = System.currentTimeMillis() + 15000;

            while (context.getMatch().getRemotePlayers().size() < EXPECTED_REMOTE_PLAYERS
                    && System.currentTimeMillis() < timeout) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            SwingUtilities.invokeLater(() -> {
                int connectedPlayers = context.getMatch().getRemotePlayers().size();

                if (connectedPlayers < EXPECTED_REMOTE_PLAYERS) {
                    view.showWaitingPlayers();
                    JOptionPane.showMessageDialog(
                            null,
                            "Solo se conectaron " + connectedPlayers + " de "
                                    + EXPECTED_REMOTE_PLAYERS + " jugadores remotos.\n"
                                    + "Verifica que todos estén conectados, usando puertos distintos\n"
                                    + "y que la red P2P esté activa en todos los equipos."
                    );
                    return;
                }

                System.out.println("Todos los jugadores remotos conectados.");
                view.showGameStarted();
                view.setOnCountdownFinished(startGame);
                view.startCountdown();
            });
        }, "WaitingPlayers-Thread").start();
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
                    "Puerto inválido. Use entre "
                            + config.getMinPort() + " y " + config.getMaxPort()
            );
        }

        return puerto;
    }
}