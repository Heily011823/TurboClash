package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.InputHandler;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;

/**
 * Representa la responsabilidad de {@code InputCoordinator} en la capa de aplicacion.
 */
public class InputCoordinator {

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;
    private final int puertoLocal;

    /**
     * Crea una nueva instancia de {@code InputCoordinator}.
     *
     * @param keyboardInput valor del parametro {@code keyboardInput}
     * @param mouseInput valor del parametro {@code mouseInput}
     * @param puertoLocal valor del parametro {@code puertoLocal}
     */
    public InputCoordinator(KeyboardInput keyboardInput, MouseInput mouseInput, int puertoLocal) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
        this.puertoLocal = puertoLocal;
    }

    /**
     * Procesa la operacion principal del metodo.
     *
     * @param player valor del parametro {@code player}
     */
    public void handle(Player player) {
        if (player == null || player.getCar() == null) return;

        InputHandler input = selectInput();
        input.update(player.getCar());
    }

    /**
     * Ejecuta la operacion {@code selectInput}.
     *
     * @return resultado de la operacion {@code selectInput}
     */
    private InputHandler selectInput() {
        return switch (puertoLocal) {
            case 5000,5002 -> keyboardInput;
            case 5001, 5003 -> mouseInput;
            default -> keyboardInput;
        };
    }
}
