package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.presentation.presenter.GamePresenter;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.ViewSynchronizer;

import javax.swing.SwingUtilities;

/**
 * Controla el ciclo principal del juego.
 */
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

        InputCoordinator inputCoordinator = new InputCoordinator(keyboard, mouse, puertoLocal);

        GamePresenter presenter = new GamePresenter(
                window,
                context.getRulesManager(),
                context.getResultManager()
        );

        networkSync.join(context, local);
        context.getNetwork().connect(context, local);

        long timeout = System.currentTimeMillis() + 5000;
        while (context.getMatch().getRemotePlayers().isEmpty()
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
                local.getCar().stop();
                inputCoordinator.handle(local);
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