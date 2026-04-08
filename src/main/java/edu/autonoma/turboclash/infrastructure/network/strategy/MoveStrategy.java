package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.util.List;

public class MoveStrategy implements IMessageStrategy {

    private final List<Player> players;

    public MoveStrategy(List<Player> players) {
        this.players = players;
    }

    @Override
    public void handle(GameMessage message) {

        for (Player p : players) {
            if (p.getId().equals(message.getPlayerId())) {
                p.syncFromNetwork(
                        message.getPosX(),
                        message.getPosY(),
                        message.getScore()
                );

                break;
            }
        }
    }
}