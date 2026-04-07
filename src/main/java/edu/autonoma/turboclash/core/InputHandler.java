package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.input.KeyboardInput;
import edu.autonoma.turboclash.input.MouseInput;
import edu.autonoma.turboclash.model.Player;

public class InputHandler {

    public void handle(Player player, KeyboardInput keyboard, MouseInput mouse, boolean useKeyboard) {
        if (useKeyboard) {
            keyboard.update(player.getCar());
        } else {
            mouse.update(player.getCar());
        }
    }
}
