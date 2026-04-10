package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.application.AuthoritativeMatchCoordinator;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;

/**
 * Representa la clase `GameStartStrategy` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameStartStrategy implements IMessageStrategy {

    private final AuthoritativeMatchCoordinator coordinator;
    /**
     * Crea una nueva instancia de `GameStartStrategy`.
     * @param coordinator valor del parametro `coordinator`
     */
    public GameStartStrategy(AuthoritativeMatchCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    /**
     * Ejecuta la operacion publica `handle`.
     * @param message valor del parametro `message`
     */
    public void handle(GameMessage message) {
        if (coordinator == null || message == null || message.getEvent() == null || message.getEvent().isBlank()) {
            return;
        }

        coordinator.applyGameStart(MessagePayloadCodec.decodeGameStart(message.getEvent()));
    }
}
