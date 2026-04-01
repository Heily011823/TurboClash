package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.List;

public class GameEngine {

    private Match match;
    private CollisionManager collisionManager;
    private List<Item> items;
    private List<Obstacle> obstacles;
    private UdpPeer peer;

    public GameEngine(Match match, CollisionManager collisionManager,
                      List<Item> items, List<Obstacle> obstacles) {

        this.match = match;
        this.collisionManager = collisionManager;
        this.items = items;
        this.obstacles = obstacles;
    }

    public void update() {
        if (match.isFinished()) return;

        Player p1 = match.getLocalPlayer();
        Player p2 = match.getRemotePlayer();

        collisionManager.process(p1, items, obstacles);
        collisionManager.process(p2, items, obstacles);

        match.check();
    }

    public void move(Player p, double x, double y) {
        p.moveCar(x, y);

        GameMessage msg = new GameMessage();
        msg.playerId = p.getId();
        msg.posX = x;
        msg.posY = y;
        msg.score = p.getCurrentPoints();

        if (peer != null) {
            peer.enviar(msg);
        }
    }
}