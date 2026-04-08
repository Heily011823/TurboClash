package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.input.InputHandler;
import edu.autonoma.turboclash.model.Player;

public class InputCoordinator {
    public void handle(Player player, InputHandler input) {
        input.update(player.getCar());
    }
}
