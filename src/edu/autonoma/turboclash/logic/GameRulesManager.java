package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Player;

public class GameRulesManager {

    private int scoreToWin;

    public GameRulesManager(int scoreToWin) {
        if (scoreToWin <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser mayor que cero.");
        }
        this.scoreToWin = scoreToWin;
    }

    public boolean checkWinByScore(Player player) {
        if (player == null) return false;
        return player.getCurrentPoints() >= scoreToWin;
    }

    public boolean checkWinByTime(TimeManager timeManager) {
        return timeManager != null && timeManager.isTimeUp();
    }

    public boolean hasWinner(Match game, TimeManager timeManager) {
        if (game == null) return false;

        return checkWinByScore(game.getLocalPlayer())
                || checkWinByScore(game.getRemotePlayer())
                || checkWinByTime(timeManager);
    }
}