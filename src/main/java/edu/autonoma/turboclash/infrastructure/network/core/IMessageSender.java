package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de {@code IMessageSender} en la infraestructura de red.
 */
public interface IMessageSender {
    /**
     * Envia {@code Mensaje}.
     *
     * @param message valor del parametro {@code message}
     * @param ip direccion IP asociada a la operacion
     * @param puerto valor del parametro {@code puerto}
     */
    void enviarMensaje(GameMessage message, String ip, int puerto);
}
