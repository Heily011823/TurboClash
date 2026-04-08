package edu.autonoma.turboclash.infrastructure.network.core;

/**
 * Define el contrato de {@code IMessageReceiver} en la infraestructura de red.
 */
public interface IMessageReceiver {
    /**
     * Inicia la escucha de la operacion principal del metodo.
     */
    void escuchar();
    /**
     * Detiene la operacion principal del metodo.
     */
    void detener();
    /**
     * Actualiza el valor de {@code Listener}.
     *
     * @param listener valor del parametro {@code listener}
     */
    void setListener(IMessageListener listener);
}
