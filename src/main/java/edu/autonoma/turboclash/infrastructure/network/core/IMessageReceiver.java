package edu.autonoma.turboclash.infrastructure.network.core;

/**
 * Define el contrato de `IMessageReceiver` dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public interface IMessageReceiver {
    void escuchar();
    void detener();
    void setListener(IMessageListener listener);
}
