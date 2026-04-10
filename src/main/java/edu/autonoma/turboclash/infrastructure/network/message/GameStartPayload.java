package edu.autonoma.turboclash.infrastructure.network.message;

/**
 * Representa la clase `GameStartPayload` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameStartPayload {
    /**
     * Expone el atributo publico `hostPort` para la colaboracion entre componentes del sistema.
     */
    public int hostPort;
    /**
     * Expone el atributo publico `sequence` para la colaboracion entre componentes del sistema.
     */
    public long sequence;
    /**
     * Expone el atributo publico `scheduledStartTime` para la colaboracion entre componentes del sistema.
     */
    public long scheduledStartTime;
    /**
     * Expone el atributo publico `connectedPlayers` para la colaboracion entre componentes del sistema.
     */
    public int connectedPlayers;
    /**
     * Expone el atributo publico `minPlayers` para la colaboracion entre componentes del sistema.
     */
    public int minPlayers;
    /**
     * Expone el atributo publico `maxPlayers` para la colaboracion entre componentes del sistema.
     */
    public int maxPlayers;
}
