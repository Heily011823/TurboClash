package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code JoinStrategy} en las estrategias de mensajeria.
 */
public class JoinStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de {@code JoinStrategy}.
     *
     * @param match valor del parametro {@code match}
     */
    public JoinStrategy(Match match) {
        this.match = match;
    }

    @Override
    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
    public void handle(GameMessage message) {


        if (message.getPlayerId().equals(match.getLocalPlayer().getId())) {
            return;
        }


        boolean exists = match.getPlayers().stream()
                .anyMatch(p -> p.getId().equals(message.getPlayerId()));

        if (exists) {
            return;
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


        match.addPlayer(newPlayer);

        System.out.println("Jugador agregado: " + message.getPlayerName());
    }
}
