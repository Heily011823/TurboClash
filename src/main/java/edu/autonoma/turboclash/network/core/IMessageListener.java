package edu.autonoma.turboclash.network.core;

import edu.autonoma.turboclash.network.message.GameMessage;

public interface IMessageListener {
    void onMessage(GameMessage message, String ip, int port);
}

