package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.input.InputHandler;
import edu.autonoma.turboclash.infrastructure.input.KeyboardInput;
import edu.autonoma.turboclash.infrastructure.input.MouseInput;

public class InputCoordinator {

    private final KeyboardInput keyboardInput;
    private final MouseInput mouseInput;

    public InputCoordinator(KeyboardInput keyboardInput, MouseInput mouseInput) {
        this.keyboardInput = keyboardInput;
        this.mouseInput = mouseInput;
    }

    public void handle(Player player) {
        if (player == null || player.getCar() == null) return;

        InputHandler selectedInput = selectInput(player);
        if (selectedInput != null) {
            selectedInput.update(player.getCar());
        }
    }

    private InputHandler selectInput(Player player) {
        String port = player.getId();

        switch (port) {
            case"5000":
            case "5003":
                return keyboardInput;

            case "5002":
            case "5004":
                return mouseInput;

            default:
                return null;
        }
    }
}