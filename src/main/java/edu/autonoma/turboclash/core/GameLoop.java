package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;
import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.view.GameWindow;
import edu.autonoma.turboclash.view.ViewSynchronizer;

public class GameLoop {

    private final int frameDelay;
    private final ObstacleSystem obstacleSystem;
    private final NetworkSync networkSync;

    public GameLoop(int frameDelay) {
        this.frameDelay = frameDelay;
        this.networkSync = new NetworkSync();

        CollisionListener listener = new SoundCollisionListener();
        this.obstacleSystem = new ObstacleSystem(listener);
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


            obstacleSystem.check(local.getCar(), context.getObstacles());


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