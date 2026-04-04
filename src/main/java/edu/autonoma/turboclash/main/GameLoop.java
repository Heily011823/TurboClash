package edu.autonoma.turboclash.main;

import edu.autonoma.turboclash.input.*;
import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.view.*;

import java.util.List;

public class GameLoop {

    private static final int FRAME_DELAY = 16;

    public void run(GameContext context,
                    GameWindow window,
                    KeyboardInput keyboard,
                    MouseInput mouse,
                    int puertoLocal) {


        boolean usaTeclado = (puertoLocal % 2 != 0);
        ViewSynchronizer viewSync = new ViewSynchronizer();

        Player local = context.match.getLocalPlayer();


        context.network.sendJoin(local);

        while (!context.match.isFinished()) {

            // 1. INPUT: Capturar entrada del usuario
            if (usaTeclado) {
                keyboard.update(local.getCar());
            } else {
                mouse.update(local.getCar());
            }

            context.engine.update();


            viewSync.sync(
                    window,
                    context.match,
                    context.obstacles,
                    context.items
            );


            context.network.sendMovement(local);


            sleep();
        }


        context.network.sendLeave(local);
        context.peer.cerrar();
    }

    private void sleep() {
        try {
            Thread.sleep(FRAME_DELAY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}