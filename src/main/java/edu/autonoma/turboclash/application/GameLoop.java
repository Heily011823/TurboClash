package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;
import edu.autonoma.turboclash.infrastructure.sound.SoundCollisionListener;
import edu.autonoma.turboclash.presentation.view.GameWindow;
import edu.autonoma.turboclash.presentation.view.ViewSynchronizer;

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
        Player local = context.getMatch().getLocalPlayer();

        networkSync.join(context, local);

        while (!context.getMatch().isFinished()) {

            if (local != null && local.getCar() != null) {

                local.getCar().updateDebuff();
                local.getCar().stop();

                keyboard.update(local.getCar());
                mouse.update(local.getCar());

                window.updateScore(local.getCurrentPoints());
            }


            context.getEngine().update();

            window.updateCars(context.getMatch().getPlayers());

            viewSync.sync(
                    window,
                    context.getMatch(),
                    context.getObstacles(),
                    context.getEngine().getItems()
            );

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