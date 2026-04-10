package edu.autonoma.turboclash.infrastructure.network.core;

/**
 * Define el contrato de `IMessageReceiver` dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public interface IMessageReceiver {
    void escuchar();
    void detener();
    void setListener(IMessageListener listener);
}
