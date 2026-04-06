package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.network.message.GameMessage;

public interface IMessageSender {
    void enviarMensaje(GameMessage message, String ip, int puerto);
}
