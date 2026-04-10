package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de `IMessageSender` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface IMessageSender {
    void enviarMensaje(GameMessage message, String ip, int puerto);
}
