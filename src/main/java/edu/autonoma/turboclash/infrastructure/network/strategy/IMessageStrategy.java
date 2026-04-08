package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de {@code IMessageStrategy} en las estrategias de mensajeria.
 */
public interface IMessageStrategy {
    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
    void handle(GameMessage message);
}
