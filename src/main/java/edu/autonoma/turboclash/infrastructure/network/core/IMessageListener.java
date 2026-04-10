package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de `IMessageListener` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface IMessageListener {
    void onMessage(GameMessage message, String ip, int port);
}

