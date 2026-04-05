package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.Player;

/**
 * GameRulesManager: El "Cerebro" de las reglas del juego.
 * Implementa las 5 reglas principales de TurboClash.
 */
public class GameRulesManager {

    private final int targetScore;

    public GameRulesManager(int targetScore) {
        this.targetScore = targetScore;
    }


    // REGLA 1: COLECCIÓN DE MONEDAS

    public void applyCoinReward(Player p) {
        if (p == null) return;
        p.updateScore(GameConstants.COIN_VALUE); // +20 puntos
    }

    // REGLA 2: CHOQUE CON OBSTÁCULOS

    public void applyObstaclePenalty(Player p) {
        if (p == null) return;
        p.updateScore(-GameConstants.OBSTACLE_PENALTY); // -10 puntos
        applySpeedDebuff(p); // Regla 4: Penalización de velocidad
    }

    // REGLA 3: COLISIÓN ENTRE JUGADORES

    public void handlePlayersCollision(Player p1, Player p2) {
        if (p1 != null) {
            p1.loseLife(); // Resta 1 corazón
            applySpeedDebuff(p1); // Regla 4
        }
        if (p2 != null) {
            p2.loseLife(); // Resta 1 corazón
            applySpeedDebuff(p2); // Regla 4
        }
    }


    // REGLA 4: PENALIZACIÓN DE VELOCIDAD (DEBUFF)

    private void applySpeedDebuff(Player p) {
        if (p != null && p.getCar() != null) {
            // Reduce al 50% por 2 segundos (definido en GameConstants)
            p.getCar().applyDebuff(
                    GameConstants.DEBUFF_SPEED_FACTOR,
                    GameConstants.DEBUFF_DURATION_MS
            );
        }
    }


    // REGLA 5: BONO DE META

    public void applyFinishBonus(Player p) {
        if (p == null || p.getCar() == null) return;


        if (!p.getCar().isFinishReached()) {
            p.updateScore(GameConstants.FINISH_LINE_BONUS); // +50 puntos
            p.getCar().setFinishReached(true);
        }
    }

    // VALIDACIÓN DE VICTORIA

    public boolean hasWon(Player p) {
        if (p == null || p.getCar() == null) return false;

        // Gana por puntaje objetivo o por cruzar la meta
        return p.getCurrentPoints() >= targetScore || p.getCar().isFinishReached();
    }
}