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

import javax.swing.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameApplication {

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

        // Registrar el local real para que en espera solo se muestre ese carro
        view.setLocalPlayer(localPlayer);

        List<PeerConfigEntry> peers = PeerConfigLoader.loadFromResource("/peers.json");

        for (PeerConfigEntry peerInfo : peers) {
            if (peerInfo.getPuerto() != puerto) {
                context.getPeer().agregarPeer(peerInfo.getIp(), peerInfo.getPuerto());
            }
        }

        int expectedRemotePlayers = (int) peers.stream()
                .filter(p -> p.getPuerto() != puerto)
                .count();

        System.out.println("Conectando a peers...");
        context.getNetwork().connect(context, localPlayer);

        // Preparar posiciones iniciales y mostrar solo el local mientras espera
        view.prepareRaceStart(context.getPlayers());
        view.updateCars(context.getPlayers());
        view.showWaitingPlayers(
                1 + context.getMatch().getRemotePlayers().size(),
                1 + expectedRemotePlayers
        );
        view.requestGameFocus();

        GameLoop loop = new GameLoop(config.getFrameDelay());
        AtomicBoolean gameStarted = new AtomicBoolean(false);

        Runnable startGame = () -> {
            if (!gameStarted.compareAndSet(false, true)) {
                return;
            }

            // Aquí sí comienza visualmente la partida
            view.showGameStarted();
            view.updateCars(context.getPlayers());

            FondoAnimadoPanel fondo = view.getBackgroundPanel();
            if (fondo != null) {
                fondo.startGame();
            }

            Thread gameThread = new Thread(() -> loop.run(context, view, keyboard, mouse, puerto));
            gameThread.setName("GameLoop-Thread");
            gameThread.setDaemon(true);
            gameThread.start();
        };

        new Thread(() -> {
            long timeout = System.currentTimeMillis() + 15000;

            while (context.getMatch().getRemotePlayers().size() < expectedRemotePlayers
                    && System.currentTimeMillis() < timeout) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                int connected = 1 + context.getMatch().getRemotePlayers().size();
                int expected = 1 + expectedRemotePlayers;

                SwingUtilities.invokeLater(() ->
                        view.showWaitingPlayers(connected, expected)
                );
            }

            SwingUtilities.invokeLater(() -> {
                int connectedPlayers = context.getMatch().getRemotePlayers().size();

                if (connectedPlayers < expectedRemotePlayers) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Solo se conectaron " + connectedPlayers + " de "
                                    + expectedRemotePlayers + " jugadores remotos."
                    );
                    return;
                }

                // Primero countdown, luego arranca el juego real
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