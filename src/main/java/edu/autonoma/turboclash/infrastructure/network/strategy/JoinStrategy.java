package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Maneja la incorporación de jugadores remotos al match.
 */
public class JoinStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = 80;

    /**
     * Debe coincidir con los carriles base del GameWindow responsive.
     */
    private static final int[] LANE_Y = {140, 280, 420, 560};

    private final Match match;

    public JoinStrategy(Match match) {
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

        Player existing = match.findRemotePlayer(messagePlayerId, messagePlayerName);

        if (existing != null) {
            updateExistingPlayer(existing, message);
            return;
        }

        Player newPlayer = createRemotePlayer(message);
        match.addPlayer(newPlayer);

        System.out.println("[JOIN] Remoto agregado: "
                + newPlayer.getName()
                + " | id=" + newPlayer.getId()
                + " | x=" + newPlayer.getCar().getX()
                + " | y=" + newPlayer.getCar().getY()
                + " | lives=" + newPlayer.getLives());
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

        Player player = new Player(messagePlayerId, messagePlayerName, car);
        player.setScore(message.getScore());

        return player;
    }

    private void updateExistingPlayer(Player existing, GameMessage message) {
        if (existing.getCar() != null) {
            double nextX = message.getPosX() > 0 ? message.getPosX() : existing.getCar().getX();
            double nextY = resolveLaneY(message);

            existing.getCar().setPosition(nextX, nextY);

            if (message.getLives() > 0) {
                existing.getCar().setLives(message.getLives());
            }
        }

        existing.setScore(message.getScore());

        System.out.println("[JOIN] Remoto actualizado: "
                + existing.getName()
                + " | x=" + existing.getCar().getX()
                + " | y=" + existing.getCar().getY()
                + " | lives=" + existing.getLives());
    }

    private boolean isLocalPlayer(Player localPlayer, String messagePlayerId, String messagePlayerName) {
        if (localPlayer == null) {
            return false;
        }

        boolean sameId =
                localPlayer.getId() != null
                        && !messagePlayerId.isBlank()
                        && localPlayer.getId().equals(messagePlayerId);

        boolean sameName =
                localPlayer.getName() != null
                        && !messagePlayerName.isBlank()
                        && localPlayer.getName().equalsIgnoreCase(messagePlayerName);

        return sameId || sameName;
    }

    private double resolveLaneY(GameMessage message) {
        int laneIndex = resolveLaneIndex(message);
        return LANE_Y[laneIndex];
    }

    private int resolveLaneIndex(GameMessage message) {
        int byPort = resolveLaneIndexByPort(message.getPort());
        if (byPort >= 0) {
            return byPort;
        }

        String playerId = safe(message.getPlayerId());
        int byId = resolveLaneIndexById(playerId);
        if (byId >= 0) {
            return byId;
        }

        return 0;
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