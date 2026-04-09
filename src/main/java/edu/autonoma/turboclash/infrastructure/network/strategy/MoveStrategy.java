package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code MoveStrategy} en las estrategias de mensajeria.
 */
public class MoveStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = 80;
    private static final double[] LANES_Y = {120, 220, 320, 420};

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
            double posY = message.getPosY() > 0 ? message.getPosY() : resolveLaneY();

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
                    + message.getPlayerName());
        }

        if (remote.getCar() != null) {
            remote.syncFromNetwork(
                    message.getPosX(),
                    message.getPosY(),
                    message.getScore()
            );

            System.out.println("Movimiento actualizado de "
                    + remote.getName()
                    + " -> X: " + message.getPosX()
                    + ", Y: " + message.getPosY()
                    + ", Score: " + message.getScore());
        }
    }

    private double resolveLaneY() {
        boolean[] used = new boolean[LANES_Y.length];

        Player local = match.getLocalPlayer();
        if (local != null && local.getCar() != null) {
            markUsedLane(local.getCar().getY(), used);
        }

        for (Player p : match.getRemotePlayers()) {
            if (p != null && p.getCar() != null) {
                markUsedLane(p.getCar().getY(), used);
            }
        }

        for (int i = 0; i < LANES_Y.length; i++) {
            if (!used[i]) {
                return LANES_Y[i];
            }
        }

        return LANES_Y[LANES_Y.length - 1];
    }

    private void markUsedLane(double y, boolean[] used) {
        for (int i = 0; i < LANES_Y.length; i++) {
            if (Math.abs(LANES_Y[i] - y) < 20) {
                used[i] = true;
                return;
            }
        }
    }
}