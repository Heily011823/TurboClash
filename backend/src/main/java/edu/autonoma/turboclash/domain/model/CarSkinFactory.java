package edu.autonoma.turboclash.domain.model;

import edu.autonoma.turboclash.config.GameConfig;

/**
 * Representa la clase `CarSkinFactory` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class CarSkinFactory {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de `CarSkinFactory`.
     * @param config valor del parametro `config`
     */
    public CarSkinFactory(GameConfig config) {
        this.config = config;
    }

    /**
     * Ejecuta la operacion publica `fromPort`.
     * @param puerto valor del parametro `puerto`
     * @return resultado de la operacion documentada
     */
    public CarSkin fromPort(int puerto) {

        int index = puerto - config.getMinPort();

        return CarSkin.fromIndex(index);
    }
}
