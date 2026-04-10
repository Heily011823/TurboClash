package edu.autonoma.turboclash;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.CollisionManager;
import edu.autonoma.turboclash.domain.services.GameRulesManager;

import java.util.ArrayList;
import java.util.List;

public final class CollisionManagerTest {

    private CollisionManagerTest() {
    }

    public static void run() {
        Player first = GameRulesManagerTest.createPlayer("5001", "One");
        Player second = GameRulesManagerTest.createPlayer("5002", "Two");
        second.getCar().setPosition(first.getCar().getX(), first.getCar().getY());

        Match match = new Match(first, new ArrayList<>(List.of(second)), 10);
        GameRulesManager rules = new GameRulesManager(10);
        CollisionManager manager = new CollisionManager(new CollisionListener() {
            @Override
            public void onItemCollision(Player player) {
            }

            @Override
            public void onObstacleCollision(Player player) {
            }

            @Override
            public void onPlayersCollision(Player p1, Player p2) {
                rules.handlePlayersCollision(p1, p2);
            }
        });

        manager.process(match, List.of(), List.of());

        GameRulesManagerTest.assertEquals(2, first.getLives(), "player one should lose one life");
        GameRulesManagerTest.assertEquals(2, second.getLives(), "player two should lose one life");
    }
}
