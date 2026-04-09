package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Estrategia para agregar jugadores remotos.
 */
public class JoinStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = 80;

    private final Match match;

    public JoinStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {
        if (message == null || match == null) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();
        String messagePlayerId = message.getPlayerId();
        String messagePlayerName = message.getPlayerName();

        if ((messagePlayerId == null || messagePlayerId.isBlank())
                && (messagePlayerName == null || messagePlayerName.isBlank())) {
            return;
        }

        if (localPlayer != null) {
            boolean sameAsLocalById =
                    localPlayer.getId() != null
                            && messagePlayerId != null
                            && localPlayer.getId().equals(messagePlayerId);

            boolean sameAsLocalByName =
                    localPlayer.getName() != null
                            && messagePlayerName != null
                            && localPlayer.getName().equalsIgnoreCase(messagePlayerName);

            if (sameAsLocalById || sameAsLocalByName) {
                return;
            }
        }

        Player existing = match.findRemotePlayer(messagePlayerId, messagePlayerName);

        if (existing != null) {
            if (existing.getCar() != null) {
                existing.getCar().setPosition(
                        message.getPosX() > 0 ? message.getPosX() : existing.getCar().getX(),
                        message.getPosY() > 0 ? message.getPosY() : existing.getCar().getY()
                );
            }
            existing.setScore(message.getScore());
            return;
        }

        String image = CarSkin.BLUE.getFileName();
        if (message.getCarSkin() != null) {
            image = message.getCarSkin().getFileName();
        }

        double posX = message.getPosX() > 0 ? message.getPosX() : START_X;
        double posY = resolveLaneYByPort(message.getPort());

        Car car = new Car(
                messagePlayerId != null ? messagePlayerId : messagePlayerName,
                posX,
                posY,
                CAR_WIDTH,
                CAR_HEIGHT,
                image
        );

        Player newPlayer = new Player(
                messagePlayerId,
                messagePlayerName,
                car
        );

        newPlayer.setScore(message.getScore());
        match.addPlayer(newPlayer);

        System.out.println("Jugador agregado: " + messagePlayerName
                + " puerto=" + message.getPort()
                + " carrilY=" + posY);
    }

    private double resolveLaneYByPort(int port) {
        return switch (port) {
            case 5001 -> 120;
            case 5002 -> 220;
            case 5003 -> 320;
            case 5004 -> 420;
            default -> 120;
        };
    }
}