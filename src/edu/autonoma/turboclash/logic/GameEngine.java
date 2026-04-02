package edu.autonoma.turboclash.logic;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.*;
import java.util.List;

public class GameEngine {

    private final Match match;
    private final CollisionManager collisionManager;
    private final List<Item> items;
    private final List<Obstacle> obstacles;
    private final List<UdpPeer> peers;

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
            p.getCar().update();
        }


        collisionManager.process(
                match.getLocalPlayer(),
                match.getRemotePlayers(),
                items,
                obstacles
        );

        match.check();
    }

    public void move(Player p, double x, double y) {
        if (p == null) return;


        p.move(x, y);
        GameMessage msg = new GameMessage(
                MessageType.MOVEMENT,
                p.getId(),
                p.getName(),
                x,
                y,
                p.getCurrentPoints(),
                System.currentTimeMillis(),
                "MOVE"
        );
        if (peers != null) {
            for (UdpPeer peer : peers) {
                peer.enviar(msg);
            }
        }
    }
    public void onMessageReceived(GameMessage msg) {
        if (msg == null) return;

        for (Player p : match.getPlayers()) {
            if (p.getId().equals(msg.playerId)) {

                p.syncFromNetwork(msg.posX, msg.posY, msg.score);
            }
        }
    }
}