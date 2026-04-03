package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Player;


public class GameRulesManager {

    private final int scoreToWin;

    public GameRulesManager(int scoreToWin) {
        if (scoreToWin <= 0) {
            throw new IllegalArgumentException("El puntaje objetivo debe ser mayor que cero.");
        }
        this.scoreToWin = scoreToWin;
    }


    // REGLA 1: MONEDAS (+20)

    public void applyCoinReward(Player player) {
        if (!isValid(player)) return;
        player.updateScore(GameConstants.COIN_VALUE);
    }


    // REGLA 2: OBSTÁCULOS (-10 + velocidad)

    public void applyObstaclePenalty(Player player) {
        if (!isValid(player)) return;

        player.updateScore(-GameConstants.OBSTACLE_PENALTY);
        applySpeedDebuff(player);
    }


    // REGLA 3: COLISIÓN ENTRE JUGADORES (-1 vida)

    public void handlePlayersCollision(Player p1, Player p2) {
        if (!isValid(p1) || !isValid(p2)) return;

        // Pierden una vida
        p1.loseLife();
        p2.loseLife();

        // Penalización de velocidad
        applySpeedDebuff(p1);
        applySpeedDebuff(p2);
    }


    // REGLA 4: DEBUFF DE VELOCIDAD (50% por 2s)

    private void applySpeedDebuff(Player player) {
        player.getCar().applySpeedModifier(
                GameConstants.DEBUFF_SPEED_FACTOR,
                GameConstants.DEBUFF_DURATION_MS
        );
    }


    // REGLA 5: BONO DE META (+50)

    public void applyFinishBonus(Player player) {
        if (!isValid(player)) return;
        player.updateScore(GameConstants.FINISH_LINE_BONUS);
    }


    // CONDICIONES DE VICTORIA
    // Prioridad 1: Llegar a la meta
    public boolean checkWinByReachFinish(Player player) {
        if (!isValid(player)) return false;

        if (player.getCar().hasReachedFinishLine()) {
            applyFinishBonus(player);
            return true;
        }
        return false;
    }

    // Prioridad 2: Puntaje
    public boolean checkWinByScore(Player player) {
        return isValid(player) && player.getCurrentPoints() >= scoreToWin;
    }


     //Determina el ganador del juego

    public Player getWinner(Match game, TimeManager timeManager) {
        if (game == null) return null;

        // 1. META (máxima prioridad)
        Player winner = checkFinishWinner(game);
        if (winner != null) return winner;

        // 2. PUNTAJE
        winner = checkScoreWinner(game);
        if (winner != null) return winner;

        // 3. TIEMPO
        if (timeManager != null && timeManager.isTimeUp()) {
            return determineWinnerByHighestScore(game);
        }

        return null;
    }


    // MÉTODOS PRIVADOS


    private Player checkFinishWinner(Match game) {
        if (checkWinByReachFinish(game.getLocalPlayer())) {
            return game.getLocalPlayer();
        }

        for (Player p : game.getRemotePlayers()) {
            if (checkWinByReachFinish(p)) {
                return p;
            }
        }
        return null;
    }

    private Player checkScoreWinner(Match game) {
        if (checkWinByScore(game.getLocalPlayer())) {
            return game.getLocalPlayer();
        }

        for (Player p : game.getRemotePlayers()) {
            if (checkWinByScore(p)) {
                return p;
            }
        }
        return null;
    }

    private Player determineWinnerByHighestScore(Match game) {
        Player winner = game.getLocalPlayer();

        for (Player p : game.getRemotePlayers()) {
            if (p.getCurrentPoints() > winner.getCurrentPoints()) {
                winner = p;
            }
        }
        return winner;
    }

    private boolean isValid(Player player) {
        return player != null;
    }
}