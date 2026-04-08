package edu.autonoma.turboclash.domain.model;


/**
 * Representa la responsabilidad de {@code Item} dentro del dominio del juego.
 */
public class Item extends GameObject {

    private final int scoreValue = 20;

    /**
     * Crea una nueva instancia de {@code Item}.
     *
     * @param id identificador asociado a la operacion
     * @param x coordenada horizontal utilizada en la operacion
     * @param y coordenada vertical utilizada en la operacion
     * @param width valor del parametro {@code width}
     * @param height valor del parametro {@code height}
     */
    public Item(String id, double x, double y, int width, int height) {
        super(id, x, y, width, height);
    }

    /**
     * Obtiene el valor de {@code ScoreValue}.
     *
     * @return valor de {@code ScoreValue}
     */
    public int getScoreValue() {
        return scoreValue;
    }
}
