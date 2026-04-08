package edu.autonoma.turboclash.infrastructure.network.core;

public interface IMessageReceiver {
    void escuchar();
    void detener();
    void setListener(IMessageListener listener);
}
