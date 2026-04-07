package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.logic.GameSpawner;
import edu.autonoma.turboclash.view.ViewSynchronizer;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.view.GameWindow;

public class GameLoop {

    private final int frameDelay;
    private final ObstacleSystem obstacleSystem;

    public GameLoop(int frameDelay) {
        this.frameDelay = frameDelay;
        this.obstacleSystem = new ObstacleSystem();
    }

    public void run(GameContext context,
                    GameWindow window,
                    KeyboardInput keyboard,
                    MouseInput mouse,
                    int puertoLocal) {

        boolean usaTeclado = (puertoLocal % 2 != 0);

        ViewSynchronizer viewSync = new ViewSynchronizer();
        Player local = context.getMatch().getLocalPlayer();

        GameSpawner spawner = new GameSpawner(
                context.getEngine().getItems(),
                context.getObstacles()
        );

        spawner.start();
        context.getNetwork().sendJoin(local);

        while (!context.getMatch().isFinished()) {

            handleInput(local, keyboard, mouse, usaTeclado);

            context.getEngine().update();

            obstacleSystem.check(local.getCar(), context.getObstacles());

            viewSync.sync(
                    window,
                    context.getMatch(),
                    context.getObstacles(),
                    context.getEngine().getItems()
            );

            context.getNetwork().sendMovement(local);

            sleep();
        }

        shutdown(context, local, spawner);
    }

    private void handleInput(Player local,
                             KeyboardInput keyboard,
                             MouseInput mouse,
                             boolean usaTeclado) {

        if (usaTeclado) {
            keyboard.update(local.getCar());
        } else {
            mouse.update(local.getCar());
        }
    }

    private void shutdown(GameContext context, Player local, GameSpawner spawner) {
        spawner.stop();
        context.getNetwork().sendLeave(local);
        context.getPeer().cerrar();
    }

    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}