package edu.autonoma.turboclash.view;

import edu.autonoma.turboclash.model.*;
import java.util.List;

/**
 * SOLID: Esta clase cumple con SRP al ser la única encargada de
 * mapear el estado del Modelo (Match/Entities) a la Vista (GameWindow).
 */
public class ViewSynchronizer {

    public void sync(GameWindow window, Match match, List<Obstacle> obstacles, List<Item> items) {
        if (window == null || match == null) return;

        Player localPlayer = match.getLocalPlayer();
        if (localPlayer == null) return;

        // 1. Sincronizar UI de estado (Puntos y Vida)
        window.updateScore(localPlayer.getCurrentPoints());

        // Aquí es donde ocurría el error: aseguramos que pasamos los tipos correctos
        window.updateHealth(localPlayer.getLives(), localPlayer.getCar());

        // 2. Sincronizar Entidades (Carros, Obstáculos, Items)
        // Usamos match.getPlayers() directamente para cumplir con la integridad del modelo
        window.updateCars(match.getPlayers());
        window.updateObstacles(obstacles);
        window.updateItems(items);

        // 3. Forzar refresco visual (Solo si el panel existe)
        if (window.getPanel() != null) {
            window.getPanel().repaint();
        }
    }
}