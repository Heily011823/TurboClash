package edu.autonoma.turboclash.infrastructure.network.message;

/**
 * Representa la clase `PlayerState` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class PlayerState {
    /**
     * Expone el atributo publico `playerId` para la colaboracion entre componentes del sistema.
     */
    public String playerId;
    /**
     * Expone el atributo publico `playerName` para la colaboracion entre componentes del sistema.
     */
    public String playerName;
    /**
     * Expone el atributo publico `posX` para la colaboracion entre componentes del sistema.
     */
    public double posX;
    /**
     * Expone el atributo publico `posY` para la colaboracion entre componentes del sistema.
     */
    public double posY;
    /**
     * Expone el atributo publico `lives` para la colaboracion entre componentes del sistema.
     */
    public int lives;
    /**
     * Expone el atributo publico `score` para la colaboracion entre componentes del sistema.
     */
    public int score;
    /**
     * Expone el atributo publico `finishReached` para la colaboracion entre componentes del sistema.
     */
    public boolean finishReached;
    /**
     * Expone el atributo publico `eliminated` para la colaboracion entre componentes del sistema.
     */
    public boolean eliminated;
    /**
     * Expone el atributo publico `finishOrder` para la colaboracion entre componentes del sistema.
     */
    public int finishOrder;
    /**
     * Expone el atributo publico `eliminationOrder` para la colaboracion entre componentes del sistema.
     */
    public int eliminationOrder;
    /**
     * Expone el atributo publico `active` para la colaboracion entre componentes del sistema.
     */
    public boolean active;
    /**
     * Expone el atributo publico `port` para la colaboracion entre componentes del sistema.
     */
    public int port;
    /**
     * Expone el atributo publico `sequence` para la colaboracion entre componentes del sistema.
     */
    public long sequence;
}
