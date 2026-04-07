package edu.autonoma.turboclash.network.core;
import edu.autonoma.turboclash.network.core.IMessageListener;

public interface IMessageReceiver {
    void escuchar();
    void detener();
    void setListener(IMessageListener listener);
}
