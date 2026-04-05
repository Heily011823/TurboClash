package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.logic.GameSpawner; // Importamos el hilito
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.view.*;

public class GameLoop {

    private static final int FRAME_DELAY = 16;

    public void run(GameContext context, GameWindow window, KeyboardInput keyboard, MouseInput mouse, int puertoLocal) {

        boolean usaTeclado = (puertoLocal % 2 != 0);
        ViewSynchronizer viewSync = new ViewSynchronizer();
        Player local = context.match.getLocalPlayer();
        GameSpawner spawner = new GameSpawner(context.engine.getItems(), context.obstacles);
        spawner.start();

        context.network.sendJoin(local);

        while (!context.match.isFinished()) {
            if (usaTeclado) keyboard.update(local.getCar());
            else mouse.update(local.getCar());

            context.engine.update();

            viewSync.sync(window, context.match, context.obstacles, context.engine.getItems());

            context.network.sendMovement(local);

            sleep();
        }
        spawner.stop();
        context.network.sendLeave(local);
        context.peer.cerrar();
    }

    private void sleep() {
        try { Thread.sleep(FRAME_DELAY); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}