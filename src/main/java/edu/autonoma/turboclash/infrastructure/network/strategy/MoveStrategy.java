package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Estrategia para sincronizar movimiento remoto.
 */
public class MoveStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = 80;

    private final Match match;

    public MoveStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {
        if (message == null || match == null) {
            return;
        }

        if ((message.getPlayerId() == null || message.getPlayerId().isBlank())
                && (message.getPlayerName() == null || message.getPlayerName().isBlank())) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();

        if (localPlayer != null) {
            boolean sameLocalById =
                    localPlayer.getId() != null
                            && message.getPlayerId() != null
                            && localPlayer.getId().equals(message.getPlayerId());

            boolean sameLocalByName =
                    localPlayer.getName() != null
                            && message.getPlayerName() != null
                            && localPlayer.getName().equalsIgnoreCase(message.getPlayerName());

            if (sameLocalById || sameLocalByName) {
                return;
            }
        }

        Player remote = match.findRemotePlayer(message.getPlayerId(), message.getPlayerName());

        if (remote == null) {
            String image = CarSkin.BLUE.getFileName();
            if (message.getCarSkin() != null) {
                image = message.getCarSkin().getFileName();
            }

            double posX = message.getPosX() > 0 ? message.getPosX() : START_X;
            double posY = resolveLaneYByPort(message.getPort());

            Car car = new Car(
                    message.getPlayerId() != null ? message.getPlayerId() : message.getPlayerName(),
                    posX,
                    posY,
                    CAR_WIDTH,
                    CAR_HEIGHT,
                    image
            );

            remote = new Player(
                    message.getPlayerId(),
                    message.getPlayerName(),
                    car
            );

            remote.setScore(message.getScore());
            match.addPlayer(remote);

            System.out.println("Jugador remoto creado por MOVEMENT: "
                    + message.getPlayerName()
                    + " puerto=" + message.getPort());
        }

        if (remote.getCar() != null) {
            remote.syncFromNetwork(
                    message.getPosX(),
                    message.getPosY(),
                    message.getScore()
            );
        }
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