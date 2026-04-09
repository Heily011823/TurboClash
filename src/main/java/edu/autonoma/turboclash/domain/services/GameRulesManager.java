package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Player;

/**
 * Administra la responsabilidad principal de {@code GameRulesManager} en los servicios de dominio.
 */
public class GameRulesManager {

    private final int targetScore;


    private int currentFinishOrder = 1;
    private int currentEliminationOrder = 1;

    /**
     * Crea una nueva instancia de {@code GameRulesManager}.
     *
     * @param targetScore valor del parametro {@code targetScore}
     */
    public GameRulesManager(int targetScore) {
        this.targetScore = targetScore;
    }


    /**
     * Ejecuta la operacion {@code applyCoinReward}.
     *
     * @param p valor del parametro {@code p}
     */
    public void applyCoinReward(Player p) {
        if (p == null) return;

        p.updateScore(GameConstants.COIN_VALUE);
        p.setHasScored(true);
    }


    /**
     * Ejecuta la operacion {@code applyObstaclePenalty}.
     *
     * @param p valor del parametro {@code p}
     */
    public void applyObstaclePenalty(Player p) {
        if (p == null) return;

        p.updateScore(-GameConstants.OBSTACLE_PENALTY);

        checkZeroLifeRule(p);
        applySpeedDebuff(p);


        checkElimination(p);
    }


    /**
     * Procesa {@code PlayersCollision}.
     *
     * @param p1 valor del parametro {@code p1}
     * @param p2 valor del parametro {@code p2}
     */
    public void handlePlayersCollision(Player p1, Player p2) {

        if (p1 != null) {
            p1.loseLife();
            applySpeedDebuff(p1);
            checkElimination(p1);
        }

        if (p2 != null) {
            p2.loseLife();
            applySpeedDebuff(p2);
            checkElimination(p2);
        }
    }


    /**
     * Ejecuta la operacion {@code checkZeroLifeRule}.
     *
     * @param p valor del parametro {@code p}
     */
    private void checkZeroLifeRule(Player p) {
        if (p.getCurrentPoints() <= 0 && p.hasScored()) {
            p.resetScore();
            p.loseLife();
        }
    }


    /**
     * Ejecuta la operacion {@code checkElimination}.
     *
     * @param p valor del parametro {@code p}
     */
    private void checkElimination(Player p) {
        if (p != null && !p.isAlive()) {
            if (p.getEliminationOrder() == Integer.MAX_VALUE) {
                p.setEliminationOrder(currentEliminationOrder++);
            }
            p.setEliminated(true);
        }
    }

    /**
     * Ejecuta la operacion {@code applySpeedDebuff}.
     *
     * @param p valor del parametro {@code p}
     */
    private void applySpeedDebuff(Player p) {
        if (p != null && p.getCar() != null) {
            p.getCar().applyDebuff(
                    GameConstants.DEBUFF_SPEED_FACTOR,
                    GameConstants.DEBUFF_DURATION_MS
            );
        }
    }

    // META
    /**
     * Ejecuta la operacion {@code applyFinishBonus}.
     *
     * @param p valor del parametro {@code p}
     */
    public void applyFinishBonus(Player p) {
        if (p == null || p.getCar() == null) return;

        if (!p.getCar().isFinishReached()) {
            p.updateScore(GameConstants.FINISH_LINE_BONUS);
            p.getCar().setFinishReached(true);

            p.setFinishReached(true);
            p.setFinishOrder(currentFinishOrder++);
        }
    }

    /**
     * Indica si {@code Won}.
     *
     * @param p valor del parametro {@code p}
     * @return true si se cumple la condicion evaluada; false en caso contrario
     */
    public boolean hasWon(Player p) {
        if (p == null || p.getCar() == null) return false;

        return p.getCurrentPoints() >= targetScore || p.getCar().isFinishReached();
    }
}
