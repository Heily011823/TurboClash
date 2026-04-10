package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.application.AuthoritativeMatchCoordinator;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;

public class GameStartStrategy implements IMessageStrategy {

    private final AuthoritativeMatchCoordinator coordinator;
    public GameStartStrategy(AuthoritativeMatchCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void handle(GameMessage message) {
        if (coordinator == null || message == null || message.getEvent() == null || message.getEvent().isBlank()) {
            return;
        }

        coordinator.applyGameStart(MessagePayloadCodec.decodeGameStart(message.getEvent()));
    }
}
