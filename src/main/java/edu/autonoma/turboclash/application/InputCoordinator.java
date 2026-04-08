package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.infrastructure.input.InputHandler;
import edu.autonoma.turboclash.domain.model.Player;

public class InputCoordinator {
    public void handle(Player player, InputHandler input) {
        input.update(player.getCar());
    }
}
