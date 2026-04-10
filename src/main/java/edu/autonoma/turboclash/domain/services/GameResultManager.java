package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la clase `GameResultManager` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public class GameResultManager {

    /**
     * Calcula el resultado requerido para calculate ranking.
     * @param players valor del parametro `players`
     * @return resultado de la operacion documentada
     */
    public List<Player> calculateRanking(List<Player> players) {
        List<Player> ranking = new ArrayList<>(players);

        ranking.sort((p1, p2) -> {
            if (p1.isFinishReached() && !p2.isFinishReached()) return -1;
            if (!p1.isFinishReached() && p2.isFinishReached()) return 1;

            if (p1.isFinishReached() && p2.isFinishReached()) {
                int cmpFinish = Integer.compare(p1.getFinishOrder(), p2.getFinishOrder());
                if (cmpFinish != 0) return cmpFinish;
            }

            if (p1.isAlive() && !p2.isAlive()) return -1;
            if (!p1.isAlive() && p2.isAlive()) return 1;

            if (!p1.isAlive() && !p2.isAlive()) {
                int cmpDeath = Integer.compare(p2.getEliminationOrder(), p1.getEliminationOrder());
                if (cmpDeath != 0) return cmpDeath;
            }

            return Integer.compare(p2.getCurrentPoints(), p1.getCurrentPoints());
        });

        return ranking;
    }
}
