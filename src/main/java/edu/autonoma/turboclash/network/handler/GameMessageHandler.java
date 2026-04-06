package edu.autonoma.turboclash.network.handler;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.message.GameMessage;

import java.util.List;

public class GameMessageHandler {

    private List<Player> remotePlayers;

    public GameMessageHandler(List<Player> remotePlayers) {
        this.remotePlayers = remotePlayers;
    }

    public void handle(GameMessage msg) {
        if (msg == null) return;

        switch (msg.getType()) {
            case PLAYER_JOINED -> handleJoin(msg);
            case MOVEMENT -> handleMovement(msg);
            case SCORE_UPDATE -> handleScore(msg);
            case PLAYER_LEFT -> handleLeft(msg);
            default -> {}
        }
    }

    private void handleJoin(GameMessage msg) {
        boolean existe = remotePlayers.stream()
                .anyMatch(p -> p.getId().equals(msg.getPlayerId()));

        if (!existe) {


            CarSkin skin = msg.getCarSkin();


            if (skin == null) {
                skin = CarSkin.RED;
            }

            Car car = new Car(
                    msg.getPlayerId(),
                    msg.getPosX(),
                    msg.getPosY(),
                    skin
            );

            Player nuevo = new Player(
                    msg.getPlayerId(),
                    msg.getPlayerName(),
                    car
            );

            remotePlayers.add(nuevo);
        }
    }

    private void handleMovement(GameMessage msg) {
        for (Player p : remotePlayers) {
            if (p.getId().equals(msg.getPlayerId())) {
                p.getCar().setPosition(msg.getPosX(), msg.getPosY());
            }
        }
    }

    private void handleScore(GameMessage msg) {
        for (Player p : remotePlayers) {
            if (p.getId().equals(msg.getPlayerId())) {
                p.setScore(msg.getScore());
            }
        }
    }

    private void handleLeft(GameMessage msg) {
        remotePlayers.removeIf(p -> p.getId().equals(msg.getPlayerId()));
    }
}