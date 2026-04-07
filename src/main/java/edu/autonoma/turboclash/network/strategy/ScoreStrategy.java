package edu.autonoma.turboclash.network.strategy;

import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.message.GameMessage;

import java.util.List;

public class ScoreStrategy implements IMessageStrategy {

    private final List<Player> players;

    public ScoreStrategy(List<Player> players) {
        this.players = players;
    }

    @Override
    public void handle(GameMessage message) {
        for (Player p : players) {
            if (p.getId().equals(message.getPlayerId())) {
                p.setScore(message.getScore());
                break;
            }
        }
    }
}