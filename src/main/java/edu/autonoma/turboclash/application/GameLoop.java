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

        // Unirse a la partida en la red
        networkSync.join(context, local);

        // Esperar hasta 5 segundos a que llegue al menos un jugador remoto
        long timeout = System.currentTimeMillis() + 5000;
        while (context.getMatch().getRemotePlayers().isEmpty()
                && System.currentTimeMillis() < timeout) {
            try {
                Thread.sleep(200); // esperar 200ms antes de volver a revisar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Jugadores remotos conectados: " + context.getMatch().getRemotePlayers().size());
        System.out.println("ANTES DEL LOOP: " + context.getMatch().isFinished());

        // Ciclo principal del juego
        while (!context.getMatch().isFinished()) {

            // Actualizar la lógica del juego
            context.getEngine().update();

            // Actualizar la presentación
            presenter.update(
                    local != null ? local.getCar() : null,
                    context.getMatch().getPlayers()
            );

            // Manejar entrada y efectos del jugador local
            if (local != null && local.getCar() != null && presenter.isMovementEnabled()) {
                local.getCar().updateDebuff();
                local.getCar().stop();

                inputCoordinator.handle(local);
            }

            // Actualizar la vista en el hilo de Swing
            SwingUtilities.invokeLater(() -> {
                viewSync.sync(
                        window,
                        context.getMatch(),
                        context.getObstacles(),
                        context.getEngine().getItems()
                );
            });

            // Sincronización de red
            networkSync.sync(context, local);

            System.out.println("ENTRO AL LOOP");
            sleep();
        }

        shutdown(context, local);
    }

    /**
     * Cierra la sesión de red y libera recursos.
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
     * Pausa entre frames según {@code frameDelay}.
     */
    private void sleep() {
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}