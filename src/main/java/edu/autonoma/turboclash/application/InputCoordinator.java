package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.InputHandler;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;

/**
 * Representa la clase `InputCoordinator` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class InputCoordinator {

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;
    private final int puertoLocal;

    /**
     * Crea una nueva instancia de `InputCoordinator`.
     * @param keyboardInput valor del parametro `keyboardInput`
     * @param mouseInput valor del parametro `mouseInput`
     * @param puertoLocal valor del parametro `puertoLocal`
     */
    public InputCoordinator(KeyboardInput keyboardInput, MouseInput mouseInput, int puertoLocal) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
        this.puertoLocal = puertoLocal;
    }

    /**
     * Ejecuta la operacion publica `handle`.
     * @param player valor del parametro `player`
     */
    public void handle(Player player) {
        if (player == null || player.getCar() == null) return;

        InputHandler input = selectInput();
        input.update(player.getCar());
    }

    private InputHandler selectInput() {
        return switch (puertoLocal) {
            case 5000,5002 -> keyboardInput;
            case 5001, 5003 -> mouseInput;
            default -> keyboardInput;
        };
    }
}
