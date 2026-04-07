package edu.autonoma.turboclash.network.handler;

import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Player;
import edu.autonoma.turboclash.network.message.GameMessage;
import edu.autonoma.turboclash.network.message.MessageType;
import edu.autonoma.turboclash.network.strategy.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMessageHandler {

    private final Map<MessageType, IMessageStrategy> strategies = new HashMap<>();

    public GameMessageHandler(List<Player> remotePlayers, Match match) {

        strategies.put(MessageType.PLAYER_JOINED, new JoinStrategy(remotePlayers, match));
        strategies.put(MessageType.MOVEMENT, new MoveStrategy(remotePlayers));
        strategies.put(MessageType.SCORE_UPDATE, new ScoreStrategy(remotePlayers));
        strategies.put(MessageType.PLAYER_LEFT, new LeaveStrategy(remotePlayers));
    }

    public void handle(GameMessage msg) {
        if (msg == null) return;

        IMessageStrategy strategy = strategies.get(msg.getType());

        if (strategy != null) {
            strategy.handle(msg);
        }
    }
}