package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;

/**
 * Representa la responsabilidad de {@code NetworkSync} en la capa de aplicacion.
 */
public class NetworkSync {

    /**
     * Sincroniza la operacion principal del metodo.
     *
     * @param context valor del parametro {@code context}
     * @param player valor del parametro {@code player}
     */
    public void sync(GameContext context, Player player) {
        context.getNetwork().sendMovement(player);
    }

    /**
     * Ejecuta la operacion {@code join}.
     *
     * @param context valor del parametro {@code context}
     * @param player valor del parametro {@code player}
     */
    public void join(GameContext context, Player player) {
        context.getNetwork().sendJoin(player);
    }

    /**
     * Ejecuta la operacion {@code leave}.
     *
     * @param context valor del parametro {@code context}
     * @param player valor del parametro {@code player}
     */
    public void leave(GameContext context, Player player) {
        context.getNetwork().sendLeave(player);
    }
}
