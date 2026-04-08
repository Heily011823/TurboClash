package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Define el contrato de escucha y coordinacion para {@code IMessageListener} en la infraestructura de red.
 */
public interface IMessageListener {
    /**
     * Atiende {@code Message}.
     *
     * @param message valor del parametro {@code message}
     * @param ip direccion IP asociada a la operacion
     * @param port valor del parametro {@code port}
     */
    void onMessage(GameMessage message, String ip, int port);
}

