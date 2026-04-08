package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public interface IMessageListener {
    void onMessage(GameMessage message, String ip, int port);
}

