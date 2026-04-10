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
    private static final double DEFAULT_Y = 140;

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

        if ((messagePlayerId.isBlank() && messagePlayerName.isBlank())
                || match.isPlayerRemoved(messagePlayerId, messagePlayerName)
                || isLocalPlayer(match.getLocalPlayer(), messagePlayerId, messagePlayerName)) {
            return;
        }

        Player remote = match.findRemotePlayer(messagePlayerId, messagePlayerName);
        if (remote == null) {
            remote = createRemotePlayer(message);
            match.addPlayer(remote);
        }

        remote.setNetworkPort(message.getPort());
        remote.setLastProcessedSequence(message.getSequence());
        remote.syncFromNetwork(
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

    private Player createRemotePlayer(GameMessage message) {
        Car car = new Car(
                !safe(message.getPlayerId()).isBlank() ? message.getPlayerId() : safe(message.getPlayerName()),
                message.getPosX() > 0 ? message.getPosX() : START_X,
                message.getPosY() > 0 ? message.getPosY() : DEFAULT_Y,
                CAR_WIDTH,
                CAR_HEIGHT,
                resolveImage(message)
        );

        Player remote = new Player(message.getPlayerId(), message.getPlayerName(), car);
        remote.setNetworkPort(message.getPort());
        return remote;
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

    private String resolveImage(GameMessage message) {
        return message.getCarSkin() != null ? message.getCarSkin().getFileName() : CarSkin.BLUE.getFileName();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
