package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

public class MoveStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = 80;

    /**
     * Debe coincidir con GameWindow.
     */
    private static final int[] LANE_Y = {80, 220, 360, 500};

    private final Match match;

    public MoveStrategy(Match match) {
        this.match = match;
    }

    @Override
    public void handle(GameMessage message) {
        if (message == null || match == null) {
            return;
        }

        String messagePlayerId = safe(message.getPlayerId());
        String messagePlayerName = safe(message.getPlayerName());

        if (messagePlayerId.isBlank() && messagePlayerName.isBlank()) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();

        if (isLocalPlayer(localPlayer, messagePlayerId, messagePlayerName)) {
            return;
        }

        Player remote = match.findRemotePlayer(messagePlayerId, messagePlayerName);

        if (remote == null) {
            remote = createRemotePlayer(message);
            match.addPlayer(remote);

            System.out.println("[MOVE] Remoto creado desde movimiento: "
                    + remote.getName()
                    + " id=" + remote.getId());
        }

        if (remote.getCar() != null) {
            double nextX = message.getPosX() > 0 ? message.getPosX() : START_X;
            double nextY = resolveLaneY(message);
            int nextLives = message.getLives() > 0 ? message.getLives() : remote.getLives();

            remote.syncFromNetwork(
                    nextX,
                    nextY,
                    message.getScore(),
                    nextLives
            );

            System.out.println("[MOVE] " + remote.getName()
                    + " x=" + nextX
                    + " y=" + nextY
                    + " score=" + message.getScore()
                    + " lives=" + nextLives);
        }
    }

    private Player createRemotePlayer(GameMessage message) {
        String messagePlayerId = safe(message.getPlayerId());
        String messagePlayerName = safe(message.getPlayerName());

        String image = resolveImage(message);
        double posX = message.getPosX() > 0 ? message.getPosX() : START_X;
        double posY = resolveLaneY(message);

        Car car = new Car(
                !messagePlayerId.isBlank() ? messagePlayerId : messagePlayerName,
                posX,
                posY,
                CAR_WIDTH,
                CAR_HEIGHT,
                image
        );

        int lives = message.getLives() > 0 ? message.getLives() : 3;
        car.setLives(lives);

        Player remote = new Player(messagePlayerId, messagePlayerName, car);
        remote.setScore(message.getScore());
        return remote;
    }

    private boolean isLocalPlayer(Player localPlayer, String messagePlayerId, String messagePlayerName) {
        if (localPlayer == null) {
            return false;
        }

        boolean sameLocalById =
                localPlayer.getId() != null
                        && !messagePlayerId.isBlank()
                        && localPlayer.getId().equals(messagePlayerId);

        boolean sameLocalByName =
                localPlayer.getName() != null
                        && !messagePlayerName.isBlank()
                        && localPlayer.getName().equalsIgnoreCase(messagePlayerName);

        return sameLocalById || sameLocalByName;
    }

    private double resolveLaneY(GameMessage message) {
        int byPort = resolveLaneIndexByPort(message.getPort());
        if (byPort >= 0) {
            return LANE_Y[byPort];
        }

        String playerId = safe(message.getPlayerId());
        int byId = resolveLaneIndexById(playerId);
        if (byId >= 0) {
            return LANE_Y[byId];
        }

        return LANE_Y[0];
    }

    private int resolveLaneIndexByPort(int port) {
        return switch (port) {
            case 5001 -> 0;
            case 5002 -> 1;
            case 5003 -> 2;
            case 5004 -> 3;
            default -> -1;
        };
    }

    private int resolveLaneIndexById(String id) {
        return switch (id) {
            case "5001", "player1", "jugador1" -> 0;
            case "5002", "player2", "jugador2" -> 1;
            case "5003", "player3", "jugador3" -> 2;
            case "5004", "player4", "jugador4" -> 3;
            default -> -1;
        };
    }

    private String resolveImage(GameMessage message) {
        if (message.getCarSkin() != null) {
            return message.getCarSkin().getFileName();
        }
        return CarSkin.BLUE.getFileName();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}