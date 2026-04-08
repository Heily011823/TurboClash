package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;

import java.util.List;

public class CollisionManager {

    private final CollisionManager collisionManager;

    public CollisionManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public void process(Match match, List<Item> items, List<Obstacle> obstacles) {
        collisionManager.process(
                match.getLocalPlayer(),
                match.getRemotePlayers(),
                items,
                obstacles
        );
    }
}
