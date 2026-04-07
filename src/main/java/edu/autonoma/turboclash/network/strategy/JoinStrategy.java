package edu.autonoma.turboclash.network.strategy;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.message.GameMessage;

import java.util.List;

public class JoinStrategy implements IMessageStrategy {

    private final List<Player> players;

    public JoinStrategy(List<Player> players) {
        this.players = players;
    }

    @Override
    public void handle(GameMessage message) {

        for (Player p : players) {
            if (p.getId().equals(message.getPlayerId())) {
                return;
            }
        }

        Car car = new Car(
                message.getPlayerId(),
                message.getPosX(),
                message.getPosY(),
                100,
                50,
                "Car_Blue.png"
        );

        Player newPlayer = new Player(
                message.getPlayerId(),
                message.getPlayerName(),
                car
        );

        players.add(newPlayer);

        System.out.println("Jugador agregado: " + message.getPlayerName());
    }
}