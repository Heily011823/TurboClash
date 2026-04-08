package edu.autonoma.turboclash.domain.services;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

import java.util.List;

public class GameEngine {

    private final Match match;
    private final PlayerService playerService;
    private final WorldService worldService;
    private final CollisionService collisionService;
    private final RuleService ruleService;

    private final List<Item> items;
    private final List<Obstacle> obstacles;

    public GameEngine(Match match,
                      PlayerService playerService,
                      WorldService worldService,
                      CollisionService collisionService,
                      RuleService ruleService,
                      List<Item> items,
                      List<Obstacle> obstacles) {

        this.match = match;
        this.playerService = playerService;
        this.worldService = worldService;
        this.collisionService = collisionService;
        this.ruleService = ruleService;
        this.items = items;
        this.obstacles = obstacles;
    }

    public void update() {
        if (match.isFinished()) return;

        playerService.updatePlayers(match);
        worldService.updateWorld(items, obstacles);
        collisionService.process(match, items, obstacles);
        ruleService.checkPlayerOut(match);

        match.check();
    }
}