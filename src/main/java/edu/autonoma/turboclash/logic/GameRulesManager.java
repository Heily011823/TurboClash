package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.Player;

public class GameRulesManager {

    private final int targetScore;

    // 🔥 NUEVO (para ranking)
    private int currentFinishOrder = 1;
    private int currentEliminationOrder = 1;

    public GameRulesManager(int targetScore) {
        this.targetScore = targetScore;
    }

    // MONEDAS
    public void applyCoinReward(Player p) {
        if (p == null) return;

        p.updateScore(GameConstants.COIN_VALUE);
        p.setHasScored(true);
    }

    // OBSTÁCULOS
    public void applyObstaclePenalty(Player p) {
        if (p == null) return;

        p.updateScore(-GameConstants.OBSTACLE_PENALTY);

        checkZeroLifeRule(p);
        applySpeedDebuff(p);

        // 🔥 NUEVO (si muere aquí)
        checkElimination(p);
    }

    // COLISIÓN ENTRE JUGADORES
    public void handlePlayersCollision(Player p1, Player p2) {

        if (p1 != null) {
            p1.loseLife();
            applySpeedDebuff(p1);
            checkElimination(p1); // 🔥 NUEVO
        }

        if (p2 != null) {
            p2.loseLife();
            applySpeedDebuff(p2);
            checkElimination(p2); // 🔥 NUEVO
        }
    }

    // REGLA NUEVA
    private void checkZeroLifeRule(Player p) {
        if (p.getCurrentPoints() <= 0 && p.hasScored()) {
            p.resetScore();
            p.loseLife();
        }
    }

    // 🔥 NUEVO: detectar eliminación
    private void checkElimination(Player p) {
        if (p != null && !p.isAlive() && !p.isEliminated()) {
            p.setEliminated(true);
            p.setEliminationOrder(currentEliminationOrder++);
        }
    }

    // DEBUFF
    private void applySpeedDebuff(Player p) {
        if (p != null && p.getCar() != null) {
            p.getCar().applyDebuff(
                    GameConstants.DEBUFF_SPEED_FACTOR,
                    GameConstants.DEBUFF_DURATION_MS
            );
        }
    }

    // META
    public void applyFinishBonus(Player p) {
        if (p == null || p.getCar() == null) return;

        if (!p.getCar().isFinishReached()) {
            p.updateScore(GameConstants.FINISH_LINE_BONUS);
            p.getCar().setFinishReached(true);

            p.setFinishReached(true);
            p.setFinishOrder(currentFinishOrder++);
        }
    }

    public boolean hasWon(Player p) {
        if (p == null || p.getCar() == null) return false;

        return p.getCurrentPoints() >= targetScore || p.getCar().isFinishReached();
    }
}