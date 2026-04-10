package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Player;

/**
 * Representa la clase `GameRulesManager` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameRulesManager {

    private final int targetScore;


    private int currentFinishOrder = 1;
    private int currentEliminationOrder = 1;

    /**
     * Crea una nueva instancia de `GameRulesManager`.
     * @param targetScore valor del parametro `targetScore`
     */
    public GameRulesManager(int targetScore) {
        this.targetScore = targetScore;
    }


    /**
     * Aplica la logica correspondiente a apply coin reward.
     * @param p valor del parametro `p`
     */
    public void applyCoinReward(Player p) {
        if (p == null) return;

        p.updateScore(GameConstants.COIN_VALUE);
        p.setHasScored(true);
    }


    /**
     * Aplica la logica correspondiente a apply obstacle penalty.
     * @param p valor del parametro `p`
     */
    public void applyObstaclePenalty(Player p) {
        if (p == null) return;

        p.updateScore(-GameConstants.OBSTACLE_PENALTY);

        checkZeroLifeRule(p);
        applySpeedDebuff(p);


        checkElimination(p);
    }


    /**
     * Procesa el evento o estado asociado a handle players collision.
     * @param p1 valor del parametro `p1`
     * @param p2 valor del parametro `p2`
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


    private void checkZeroLifeRule(Player p) {
        if (p.getCurrentPoints() <= 0 && p.hasScored()) {
            p.resetScore();
            p.loseLife();
        }
    }


    private void checkElimination(Player p) {
        if (p != null && !p.isAlive()) {
            if (p.getEliminationOrder() == Integer.MAX_VALUE) {
                p.setEliminationOrder(currentEliminationOrder++);
            }
            p.setEliminated(true);
        }
    }

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
     * Aplica la logica correspondiente a apply finish bonus.
     * @param p valor del parametro `p`
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
     * Indica la condicion evaluada por `hasWon`.
     * @param p valor del parametro `p`
     * @return resultado de la operacion documentada
     */
    public boolean hasWon(Player p) {
        if (p == null || p.getCar() == null) return false;

        return p.getCurrentPoints() >= targetScore || p.getCar().isFinishReached();
    }
}
