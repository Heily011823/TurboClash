package edu.autonoma.turboclash.infrastructure.network.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la clase `MatchSnapshot` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class MatchSnapshot {
    /**
     * Expone el atributo publico `hostPort` para la colaboracion entre componentes del sistema.
     */
    public int hostPort;
    /**
     * Expone el atributo publico `sequence` para la colaboracion entre componentes del sistema.
     */
    public long sequence;
    /**
     * Expone el atributo publico `started` para la colaboracion entre componentes del sistema.
     */
    public boolean started;
    /**
     * Expone el atributo publico `finished` para la colaboracion entre componentes del sistema.
     */
    public boolean finished;
    /**
     * Expone el atributo publico `scheduledStartTime` para la colaboracion entre componentes del sistema.
     */
    public long scheduledStartTime;
    /**
     * Expone el atributo publico `remainingMillis` para la colaboracion entre componentes del sistema.
     */
    public long remainingMillis;
    /**
     * Expone el atributo publico `gameOverReason` para la colaboracion entre componentes del sistema.
     */
    public String gameOverReason;
    /**
     * Expone el atributo publico `winnerId` para la colaboracion entre componentes del sistema.
     */
    public String winnerId;
    /**
     * Expone el atributo publico `players` para la colaboracion entre componentes del sistema.
     */
    public List<PlayerState> players = new ArrayList<>();
    /**
     * Expone el atributo publico `items` para la colaboracion entre componentes del sistema.
     */
    public List<WorldObjectState> items = new ArrayList<>();
    /**
     * Expone el atributo publico `obstacles` para la colaboracion entre componentes del sistema.
     */
    public List<WorldObjectState> obstacles = new ArrayList<>();
}
