package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public interface IMessageStrategy {
    void handle(GameMessage message);
}
