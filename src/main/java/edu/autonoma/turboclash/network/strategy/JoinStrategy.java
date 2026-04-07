package edu.autonoma.turboclash.network.strategy;

import edu.autonoma.turboclash.model.*;
import edu.autonoma.turboclash.network.message.GameMessage;

import java.util.List;

public class JoinStrategy implements IMessageStrategy {

    private final List<Player> players;
    private final Match match;

    public JoinStrategy(List<Player> players, Match match) {
        this.players = players;
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {


        if (message.getPlayerId().equals(match.getLocalPlayer().getId())) {
            return;
        }


        for (Player p : players) {
            if (p.getId().equals(message.getPlayerId())) {
                return;
            }
        }


        String image = "Car_Blue.png";

        if (message.getCarSkin() != null) {
            image = message.getCarSkin().name() + ".png";

        }

        Car car = new Car(
                message.getPlayerId(),
                message.getPosX(),
                message.getPosY(),
                100,
                50,
                image
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