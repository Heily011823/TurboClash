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

        System.out.println("GameLoop iniciado.");
        System.out.println("Jugadores remotos conectados al iniciar loop: "
                + context.getMatch().getRemotePlayers().size());

        while (!context.getMatch().isFinished()) {

            context.getEngine().update();

            presenter.update(
                    local != null ? local.getCar() : null,
                    context.getMatch().getPlayers()
            );

            if (local != null && local.getCar() != null && presenter.isMovementEnabled()) {
                local.getCar().updateDebuff();

                if (puertoLocal == 5001 || puertoLocal == 5002) {
                    keyboard.update(local.getCar());
                } else if (puertoLocal == 5003 || puertoLocal == 5004) {
                    mouse.update(local.getCar());
                } else {
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
        System.out.println("GameLoop finalizado.");
    }

    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
