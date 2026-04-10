package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de `IMessageSender` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public interface IMessageSender {
    void enviarMensaje(GameMessage message, String ip, int puerto);
}
