package edu.autonoma.turboclash.infrastructure.network.factory;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

/**
 * Crea y configura instancias relacionadas con {@code GameMessageFactory} en la fabrica de mensajes de red.
 */
public class GameMessageFactory {

    /**
     * Crea la operacion principal del metodo.
     *
     * @param player valor del parametro {@code player}
     * @param type valor del parametro {@code type}
     * @return resultado de la operacion {@code create}
     */
    public GameMessage create(Player player, MessageType type) {
        GameMessage msg = new GameMessage();

        msg.setType(type);
        msg.setPlayerId(player.getId());
        msg.setPlayerName(player.getName());
        msg.setPosX(player.getCar().getX());
        msg.setPosY(player.getCar().getY());
        msg.setTime(System.currentTimeMillis());


        return msg;
    }
}
