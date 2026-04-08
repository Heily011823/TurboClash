package edu.autonoma.turboclash.infrastructure.network.handler;

import edu.autonoma.turboclash.infrastructure.network.strategy.*;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa la responsabilidad de {@code GameMessageHandler} en el procesamiento de mensajes de red.
 */
public class GameMessageHandler {

    private final Map<MessageType, IMessageStrategy> strategies = new HashMap<>();

    /**
     * Crea una nueva instancia de {@code GameMessageHandler}.
     *
     * @param match valor del parametro {@code match}
     */
    public GameMessageHandler(Match match) {

        strategies.put(MessageType.PLAYER_JOINED, new JoinStrategy(match));
        strategies.put(MessageType.MOVEMENT, new MoveStrategy(match));
        strategies.put(MessageType.SCORE_UPDATE, new ScoreStrategy(match));
        strategies.put(MessageType.PLAYER_LEFT, new LeaveStrategy(match));
    }

    /**
     * Procesa la operacion principal del metodo.
     *
     * @param msg valor del parametro {@code msg}
     */
    public void handle(GameMessage msg) {
        if (msg == null) return;

        IMessageStrategy strategy = strategies.get(msg.getType());

        if (strategy != null) {
            strategy.handle(msg);
        }
    }

}
