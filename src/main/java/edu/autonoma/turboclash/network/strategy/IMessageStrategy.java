package edu.autonoma.turboclash.network.strategy;

import edu.autonoma.turboclash.network.message.GameMessage;

public interface IMessageStrategy {
    void handle(GameMessage message);
}
