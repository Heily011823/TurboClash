package edu.autonoma.turboclash.domain.model;


/**
 * Representa la clase `Item` y define su responsabilidad dentro del sistema.
 * @author Elizabeth Meneses Muñoz </elizabeth.menesesm@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class Item extends GameObject {

    private final int scoreValue = 20;

    /**
     * Crea una nueva instancia de `Item`.
     * @param id valor del parametro `id`
     * @param x valor del parametro `x`
     * @param y valor del parametro `y`
     * @param width valor del parametro `width`
     * @param height valor del parametro `height`
     */
    public Item(String id, double x, double y, int width, int height) {
        super(id, x, y, width, height);
    }

    /**
     * Obtiene el valor asociado a `getScoreValue`.
     * @return resultado de la operacion documentada
     */
    public int getScoreValue() {
        return scoreValue;
    }
}
