package edu.autonoma.turboclash.network;

import edu.autonoma.turboclash.model.*;

import java.util.List;

public class GameMessageHandler {

    private List<Player> remotePlayers;

    public GameMessageHandler(List<Player> remotePlayers) {
        this.remotePlayers = remotePlayers;
    }

    public void handle(GameMessage msg) {

        switch (msg.type) {

            case PLAYER_JOINED -> handleJoin(msg);

            case MOVEMENT -> handleMovement(msg);

            case SCORE_UPDATE -> handleScore(msg);

            case PLAYER_LEFT -> handleLeft(msg);

            default -> {}
        }
    }

    private void handleJoin(GameMessage msg) {
        boolean existe = remotePlayers.stream()
                .anyMatch(p -> p.getId().equals(msg.playerId));

        if (!existe) {
            Car car = new Car("c_" + msg.playerId, msg.posX, msg.posY, 40, 40);
            Player nuevo = new Player(msg.playerId, msg.playerName, car);
            remotePlayers.add(nuevo);
        }
    }

    private void handleMovement(GameMessage msg) {
        for (Player p : remotePlayers) {
            if (p.getId().equals(msg.playerId)) {
                p.getCar().moveTo(msg.posX, msg.posY);
            }
        }
    }

    private void handleScore(GameMessage msg) {
        for (Player p : remotePlayers) {
            if (p.getId().equals(msg.playerId)) {
                p.setScore(msg.score);
            }
        }
    }

    private void handleLeft(GameMessage msg) {
        remotePlayers.removeIf(p -> p.getId().equals(msg.playerId));
    }
}