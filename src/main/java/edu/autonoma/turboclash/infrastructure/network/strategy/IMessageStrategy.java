package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de `IMessageStrategy` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface IMessageStrategy {
    void handle(GameMessage message);
}
