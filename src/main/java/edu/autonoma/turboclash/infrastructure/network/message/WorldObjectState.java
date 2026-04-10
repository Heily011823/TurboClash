package edu.autonoma.turboclash.infrastructure.network.message;

/**
 * Representa la clase `WorldObjectState` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class WorldObjectState {
    /**
     * Expone el atributo publico `id` para la colaboracion entre componentes del sistema.
     */
    public String id;
    /**
     * Expone el atributo publico `posX` para la colaboracion entre componentes del sistema.
     */
    public double posX;
    /**
     * Expone el atributo publico `posY` para la colaboracion entre componentes del sistema.
     */
    public double posY;
    /**
     * Expone el atributo publico `width` para la colaboracion entre componentes del sistema.
     */
    public int width;
    /**
     * Expone el atributo publico `height` para la colaboracion entre componentes del sistema.
     */
    public int height;
    /**
     * Expone el atributo publico `visible` para la colaboracion entre componentes del sistema.
     */
    public boolean visible;
    /**
     * Expone el atributo publico `processed` para la colaboracion entre componentes del sistema.
     */
    public boolean processed;
    /**
     * Expone el atributo publico `type` para la colaboracion entre componentes del sistema.
     */
    public String type;
}
