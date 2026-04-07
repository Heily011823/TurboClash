package edu.autonoma.turboclash.network.strategy;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.message.GameMessage;

import java.util.List;

public class LeaveStrategy implements IMessageStrategy {

    private final List<Player> players;

    public LeaveStrategy(List<Player> players) {
        this.players = players;
    }

    @Override
    public void handle(GameMessage message) {
        players.removeIf(p -> p.getId().equals(message.getPlayerId()));
    }
}