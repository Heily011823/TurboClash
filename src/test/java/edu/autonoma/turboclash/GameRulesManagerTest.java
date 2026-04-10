package edu.autonoma.turboclash;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameRulesManager;

public final class GameRulesManagerTest {

    private GameRulesManagerTest() {
    }

    public static void run() {
        GameRulesManager rules = new GameRulesManager(10);
        Player player = createPlayer("1", "Host");

        rules.applyCoinReward(player);
        assertTrue(player.getCurrentPoints() > 0, "coin reward should increase score");

        player.setLives(1);
        player.setScore(0);
        player.setHasScored(true);
        rules.applyObstaclePenalty(player);

        assertEquals(0, player.getLives(), "obstacle penalty should consume last life");
        assertTrue(player.isEliminated(), "player should be eliminated at zero lives");
    }

    static Player createPlayer(String id, String name) {
        Car car = new Car(id, 100, 100, 80, 40, "/image/Car_Blue.png");
        Player player = new Player(id, name, car);
        player.setNetworkPort(Integer.parseInt(id));
        return player;
    }

    static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }

    static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " expected=" + expected + " actual=" + actual);
        }
    }
}
