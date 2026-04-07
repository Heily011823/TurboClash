package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.model.Player;

public class NetworkSync {

    public void sync(GameContext context, Player player) {
        context.getNetwork().sendMovement(player);
    }

    public void join(GameContext context, Player player) {
        context.getNetwork().sendJoin(player);
    }

    public void leave(GameContext context, Player player) {
        context.getNetwork().sendLeave(player);
    }
}
