package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de `IMessageStrategy` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public interface IMessageStrategy {
    void handle(GameMessage message);
}
