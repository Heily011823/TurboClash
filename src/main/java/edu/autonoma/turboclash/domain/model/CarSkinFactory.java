package edu.autonoma.turboclash.domain.model;

import edu.autonoma.turboclash.config.GameConfig;

/**
 * Crea y configura instancias relacionadas con {@code CarSkinFactory} dentro del dominio del juego.
 */
public class CarSkinFactory {

    private final GameConfig config;

    /**
     * Crea una nueva instancia de {@code CarSkinFactory}.
     *
     * @param config valor del parametro {@code config}
     */
    public CarSkinFactory(GameConfig config) {
        this.config = config;
    }

    /**
     * Ejecuta la operacion {@code fromPort}.
     *
     * @param puerto valor del parametro {@code puerto}
     * @return resultado de la operacion {@code fromPort}
     */
    public CarSkin fromPort(int puerto) {

        int index = puerto - config.getMinPort();

        return CarSkin.fromIndex(index);
    }
}
