package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;

import java.util.List;

public class GameEngine {

    private Match match;
    private CollisionManager collisionManager;
    private List<Item> items;
    private List<Obstacle> obstacles;


    private List<UdpPeer> peers;

    public GameEngine(Match match,
                      CollisionManager collisionManager,
                      List<Item> items,
                      List<Obstacle> obstacles,
                      List<UdpPeer> peers) {

        this.match = match;
        this.collisionManager = collisionManager;
        this.items = items;
        this.obstacles = obstacles;
        this.peers = peers;
    }

    public void update() {
        if (match.isFinished()) return;


        for (Player p : match.getPlayers()) {
            collisionManager.process(p, items, obstacles);
        }

        match.check();
    }


    public void move(Player p, double x, double y) {
        if (p == null) return;

        p.move(x, y);

        GameMessage msg = new GameMessage();
        msg.playerId = p.getId();
        msg.posX = x;
        msg.posY = y;
        msg.score = p.getCurrentPoints();


        if (peers != null) {
            for (UdpPeer peer : peers) {
                peer.enviar(msg);
            }
        }
    }


    public void onMessageReceived(GameMessage msg) {

        for (Player p : match.getPlayers()) {

            if (p.getId().equals(msg.playerId)) {
                p.move(msg.posX, msg.posY);
                p.updateScore(msg.score);
            }
        }
    }
}