package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.presentation.presenter.GamePresenter;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.ViewSynchronizer;

import javax.swing.*;

/**
 * Controla el ciclo principal asociado a {@code GameLoop} en la capa de aplicacion.
 */
public class GameLoop {

    private final int frameDelay;
    private final NetworkSync networkSync;

    /**
     * Crea una nueva instancia de {@code GameLoop}.
     *
     * @param frameDelay valor del parametro {@code frameDelay}
     */
    public GameLoop(int frameDelay) {
        this.frameDelay = frameDelay;
        this.networkSync = new NetworkSync();
    }

    /**
     * Ejecuta la tarea principal de {@code GameLoop}.
     *
     * @param context valor del parametro {@code context}
     * @param window valor del parametro {@code window}
     * @param keyboard valor del parametro {@code keyboard}
     * @param mouse valor del parametro {@code mouse}
     * @param puertoLocal valor del parametro {@code puertoLocal}
     */
    public void run(GameContext context,
                    GameWindow window,
                    KeyboardInput keyboard,
                    MouseInput mouse,
                    int puertoLocal) {

        ViewSynchronizer viewSync = new ViewSynchronizer();
        Player local = context.getMatch().getLocalPlayer();

        InputCoordinator inputCoordinator = new InputCoordinator(keyboard, mouse, puertoLocal);

        GamePresenter presenter = new GamePresenter(
                window,
                context.getRulesManager(),
                context.getResultManager()
        );

        networkSync.join(context, local);
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

            SwingUtilities.invokeLater(() -> {
                viewSync.sync(
                        window,
                        context.getMatch(),
                        context.getObstacles(),
                        context.getEngine().getItems()
                );
            });

            networkSync.sync(context, local);
            System.out.println("ENTRO AL LOOP");
            sleep();
        }

        shutdown(context, local);
    }

    /**
     * Ejecuta la operacion {@code shutdown}.
     *
     * @param context valor del parametro {@code context}
     * @param local valor del parametro {@code local}
     */
    private void shutdown(GameContext context, Player local) {
        networkSync.leave(context, local);

        if (context.getPeer() != null) {
            context.getPeer().cerrar();
        }
    }

    /**
     * Ejecuta la operacion {@code sleep}.
     */
    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
