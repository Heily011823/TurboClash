package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.presentation.view.GameViewport;

public class JoinStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;
    private static final double START_X = GameViewport.CAR_START_X;
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

        if ((messagePlayerId.isBlank() && messagePlayerName.isBlank())
                || match.isPlayerRemoved(messagePlayerId, messagePlayerName)
                || isLocalPlayer(match.getLocalPlayer(), messagePlayerId, messagePlayerName)) {
            return;
        }

        Player existing = match.findRemotePlayer(messagePlayerId, messagePlayerName);
        if (existing == null) {
            Player newPlayer = createRemotePlayer(message);
            match.addPlayer(newPlayer);
            return;
        }

        syncPlayer(existing, message);
    }

    private Player createRemotePlayer(GameMessage message) {
        String image = resolveImage(message);
        double posX = message.getPosX() > 0 ? message.getPosX() : START_X;
        double posY = message.getPosY() > 0 ? message.getPosY() : resolveFallbackLaneY(message.getPort());

        Car car = new Car(
                !safe(message.getPlayerId()).isBlank() ? message.getPlayerId() : safe(message.getPlayerName()),
                posX,
                posY,
                CAR_WIDTH,
                CAR_HEIGHT,
                image
        );
        car.setLives(Math.max(0, message.getLives()));
        car.setFinishReached(message.isFinishReached());

        Player player = new Player(message.getPlayerId(), message.getPlayerName(), car);
        player.setNetworkPort(message.getPort());
        player.setLastProcessedSequence(message.getSequence());
        player.syncFromNetwork(
                posX,
                posY,
                message.getScore(),
                Math.max(0, message.getLives()),
                message.isFinishReached(),
                message.isEliminated(),
                message.getFinishOrder(),
                message.getEliminationOrder()
        );
        return player;
    }

    private void syncPlayer(Player existing, GameMessage message) {
        existing.setNetworkPort(message.getPort());
        existing.setLastProcessedSequence(message.getSequence());
        existing.syncFromNetwork(
                message.getPosX(),
                message.getPosY(),
                message.getScore(),
                Math.max(0, message.getLives()),
                message.isFinishReached(),
                message.isEliminated(),
                message.getFinishOrder(),
                message.getEliminationOrder()
        );
    }

    private boolean isLocalPlayer(Player localPlayer, String messagePlayerId, String messagePlayerName) {
        if (localPlayer == null) {
            return false;
        }

        boolean sameId = localPlayer.getId() != null
                && !messagePlayerId.isBlank()
                && localPlayer.getId().equals(messagePlayerId);

        boolean sameName = localPlayer.getName() != null
                && !messagePlayerName.isBlank()
                && localPlayer.getName().equalsIgnoreCase(messagePlayerName);

        return sameId || sameName;
    }

    private double resolveFallbackLaneY(int port) {
        return switch (port) {
            case 5001 -> GameViewport.laneY(0);
            case 5002 -> GameViewport.laneY(1);
            case 5003 -> GameViewport.laneY(2);
            case 5004 -> GameViewport.laneY(3);
            default -> GameViewport.laneY(0);
        };
    }

    private String resolveImage(GameMessage message) {
        return message.getCarSkin() != null ? message.getCarSkin().getFileName() : CarSkin.BLUE.getFileName();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
