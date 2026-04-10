package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.application.AuthoritativeMatchCoordinator;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;

/**
 * Representa la clase `SyncStrategy` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class SyncStrategy implements IMessageStrategy {

    private final AuthoritativeMatchCoordinator coordinator;
    /**
     * Crea una nueva instancia de `SyncStrategy`.
     * @param coordinator valor del parametro `coordinator`
     */
    public SyncStrategy(AuthoritativeMatchCoordinator coordinator) {
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

        coordinator.applySnapshot(MessagePayloadCodec.decodeSnapshot(message.getEvent()));
    }
}
