package edu.autonoma.turboclash.logic;
import model.Item;
import model.Player;
import model.Obstacle;

public class ScoreManager {

    // Suma puntos cuando recoge un item
    public void addItemPoints(Player player, Item item) {
        if (player == null || item == null) return;
        player.addPoints(item.getValue());
    }

    // Resta puntos por colisión
    public void subtractObstaclePoints(Player player, Obstacle obstacle) {
        if (player == null || obstacle == null) return;
        player.removePoints(obstacle.getPenalty());
    }

    // Determina el ganador por puntaje
    public Player getWinner(Player p1, Player p2) {
        if (p1 == null) return p2;
        if (p2 == null) return p1;

        int s1 = p1.getScore().getPoints();
        int s2 = p2.getScore().getPoints();

        if (s1 > s2) return p1;
        if (s2 > s1) return p2;
        return null; // empate
    }
}