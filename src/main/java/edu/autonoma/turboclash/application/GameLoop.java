package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.presentation.presenter.GamePresenter;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.ViewSynchronizer;

import javax.swing.SwingUtilities;

public class GameLoop {

    private final int frameDelay;
    private final NetworkSync networkSync;

    // Si juegan 4 en total, cada cliente debe ver 3 remotos
    private static final int EXPECTED_REMOTE_PLAYERS = 3;

    public GameLoop(int frameDelay) {
        this.frameDelay = frameDelay;
        this.networkSync = new NetworkSync();
    }

    public void run(GameContext context,
                    GameWindow window,
                    KeyboardInput keyboard,
                    MouseInput mouse,
                    int puertoLocal) {

        ViewSynchronizer viewSync = new ViewSynchronizer();
        Player local = context.getLocalPlayer();

        GamePresenter presenter = new GamePresenter(
                window,
                context.getRulesManager(),
                context.getResultManager()
        );

        networkSync.join(context, local);

        // Esperar un poco a que lleguen los remotos
        long timeout = System.currentTimeMillis() + 10000;
        while (context.getMatch().getRemotePlayers().size() < EXPECTED_REMOTE_PLAYERS
                && System.currentTimeMillis() < timeout) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Jugadores remotos conectados: " + context.getMatch().getRemotePlayers().size());
        System.out.println("ANTES DEL LOOP: " + context.getMatch().isFinished());

        while (!context.getMatch().isFinished()) {

            context.getEngine().update();

            presenter.update(
                    local != null ? local.getCar() : null,
                    context.getMatch().getPlayers()
            );

            if (local != null && local.getCar() != null && presenter.isMovementEnabled()) {
                local.getCar().updateDebuff();

                // 5001 y 5002 -> teclado
                // 5003 y 5004 -> mouse
                if (puertoLocal == 5001 || puertoLocal == 5002) {
                    keyboard.update(local.getCar());
                } else if (puertoLocal == 5003 || puertoLocal == 5004) {
                    mouse.update(local.getCar());
                } else {
                    // respaldo por si usan otro puerto
                    keyboard.update(local.getCar());
                }
            }

            SwingUtilities.invokeLater(() -> viewSync.sync(
                    window,
                    context.getMatch(),
                    context.getObstacles(),
                    context.getEngine().getItems()
            ));

            networkSync.sync(context, local);

            sleep();
        }

        shutdown(context, local);
    }

    private void shutdown(GameContext context, Player local) {
        networkSync.leave(context, local);

        if (context.getPeer() != null) {
            context.getPeer().cerrar();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}