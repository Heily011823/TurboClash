package edu.autonoma.turboclash;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;

import java.util.ArrayList;

/**
 * Representa la clase `MatchSequenceTest` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public final class MatchSequenceTest {

    private MatchSequenceTest() {
    }

    /**
     * Ejecuta la operacion publica `run`.
     */
    public static void run() {
        Player host = GameRulesManagerTest.createPlayer("5001", "Host");
        Match match = new Match(host, new ArrayList<>(), 10);

        GameRulesManagerTest.assertTrue(match.isPacketFresh("5002", "Remote", 2L), "first packet should be fresh");
        GameRulesManagerTest.assertTrue(!match.isPacketFresh("5002", "Remote", 1L), "older packet should be discarded");
        match.markPlayerRemoved("5002", "Remote");
        GameRulesManagerTest.assertTrue(match.isPlayerRemoved("5002", "Remote"), "removed player tombstone should be kept");
    }
}
