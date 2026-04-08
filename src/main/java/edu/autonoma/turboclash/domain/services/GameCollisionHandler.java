package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.events.CollisionListener;
import edu.autonoma.turboclash.domain.model.Player;

public class GameCollisionHandler implements CollisionListener {

    private final GameRulesManager rules;

    public GameCollisionHandler(GameRulesManager rules) {
        this.rules = rules;
    }

    @Override
    public void onItemCollision(Player player) {
        rules.applyCoinReward(player);
    }

    @Override
    public void onObstacleCollision(Player player) {
        rules.applyObstaclePenalty(player);
    }

    @Override
    public void onPlayersCollision(Player p1, Player p2) {
        rules.handlePlayersCollision(p1, p2);
    }
}