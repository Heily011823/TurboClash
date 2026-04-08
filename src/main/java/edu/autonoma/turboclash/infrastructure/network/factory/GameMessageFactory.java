package edu.autonoma.turboclash.infrastructure.network.factory;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

public class GameMessageFactory {

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