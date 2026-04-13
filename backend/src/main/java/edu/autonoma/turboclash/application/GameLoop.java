package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.ViewSynchronizer;

import javax.swing.SwingUtilities;

/**
 * Representa la clase `GameLoop` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class GameLoop {

    private final int frameDelay;
    private final NetworkSync networkSync;

    /**
     * Crea una nueva instancia de `GameLoop`.
     * @param frameDelay valor del parametro `frameDelay`
     */
    public GameLoop(int frameDelay) {
        this.frameDelay = frameDelay;
        this.networkSync = new NetworkSync();
    }

    /**
     * Ejecuta la operacion publica `run`.
     * @param context valor del parametro `context`
     * @param window valor del parametro `window`
     * @param keyboard valor del parametro `keyboard`
     * @param mouse valor del parametro `mouse`
     * @param puertoLocal valor del parametro `puertoLocal`
     */
    public void run(GameContext context,
                    GameWindow window,
                    KeyboardInput keyboard,
                    MouseInput mouse,
                    int puertoLocal) {

        ViewSynchronizer viewSync = new ViewSynchronizer();
        Player local = context.getLocalPlayer();
        long lastCountdownStart = -1L;
        boolean resultShown = false;
        boolean backgroundStarted = false;

        while (!context.getMatch().isFinished()) {
            context.getCoordinator().updateHostAuthority(window);
            context.getCoordinator().maybeStartMatch();

            if (context.getMatch().getScheduledStartTime() > 0
                    && context.getMatch().getScheduledStartTime() != lastCountdownStart) {
                long scheduledStart = context.getMatch().getScheduledStartTime();
                lastCountdownStart = scheduledStart;
                SwingUtilities.invokeLater(() -> window.startSynchronizedCountdown(scheduledStart));
            }

            boolean movementEnabled = context.getMatch().isStarted()
                    && local != null
                    && local.getCar() != null
                    && local.getCar().isActive()
                    && !local.isEliminated();

            if (movementEnabled) {
                local.getCar().updateDebuff();

                if (puertoLocal == 5003 || puertoLocal == 5004) {
                    mouse.update(local.getCar());
                } else {
                    keyboard.update(local.getCar());
                }
            }

            if (context.getMatch().isStarted() && !backgroundStarted) {
                backgroundStarted = true;
                SwingUtilities.invokeLater(() -> {
                    window.showGameStarted();
                    if (window.getBackgroundPanel() != null) {
                        window.getBackgroundPanel().startGame();
                    }
                });
            }

            SwingUtilities.invokeLater(() -> viewSync.sync(
                    window,
                    context.getMatch(),
                    context.getEngine().getObstacles(),
                    context.getEngine().getItems()
            ));

            networkSync.sync(context, local);
            sleep();
        }

        if (!resultShown) {
            resultShown = true;
            SwingUtilities.invokeLater(() -> window.showGameResult(context.getMatch().getRanking()));
        }

        shutdown(context, local);
    }

    private void shutdown(GameContext context, Player local) {
        if (context == null || context.getMatch().isFinished()) {
            return;
        }
        context.getNetwork().sendLeave(local);
    }

    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
