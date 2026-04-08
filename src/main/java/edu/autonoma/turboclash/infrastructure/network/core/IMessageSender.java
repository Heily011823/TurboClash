package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public interface IMessageSender {
    void enviarMensaje(GameMessage message, String ip, int puerto);
}
