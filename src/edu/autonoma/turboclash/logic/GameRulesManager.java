package edu.autonoma.turboclash.logic;
import model.Player;
import model.GameObject;

public class GameRulesManager {

    private int scoreToWin;

    public GameRulesManager(int scoreToWin) {
        // Validamos que el puntaje objetivo sea válido
        if (scoreToWin <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser mayor que cero.");
        }
        this.scoreToWin = scoreToWin;
    }

    // Verifica si un jugador gana por puntaje
    public boolean checkWinByScore(Player player) {
        if (player == null || player.getScore() == null) return false;
        return player.getScore().getPoints() >= scoreToWin;
    }

    // Verifica si el tiempo terminó
    public boolean checkWinByTime(TimeManager timeManager) {
        return timeManager != null && timeManager.isTimeUp();
    }

    // Verifica si hay ganador
    public boolean hasWinner(GameObject game, TimeManager timeManager) {
        if (game == null) return false;

        return checkWinByScore(game.getLocalPlayer())
                || checkWinByScore(game.getRemotePlayer())
                || checkWinByTime(timeManager);
    }
}