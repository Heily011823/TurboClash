package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.CarSkin;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code JoinStrategy} en las estrategias de mensajeria.
 */
public class JoinStrategy implements IMessageStrategy {

    private static final int CAR_WIDTH = 100;
    private static final int CAR_HEIGHT = 50;

    private static final double START_X = 80;

    // 4 carriles verticales fijos
    private static final double[] LANES_Y = {120, 220, 320, 420};

    private final Match match;

    /**
     * Crea una nueva instancia de {@code JoinStrategy}.
     *
     * @param match valor del parametro {@code match}
     */
    public JoinStrategy(Match match) {
        this.match = match;
    }

    /**
     * Procesa la operacion principal del metodo.
     *
     * @param message valor del parametro {@code message}
     */
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
                double x = message.getPosX() > 0 ? message.getPosX() : existing.getCar().getX();
                double y = message.getPosY() > 0 ? message.getPosY() : existing.getCar().getY();
                existing.getCar().setPosition(x, y);
            }
            existing.setScore(message.getScore());
            return;
        }

        String image = CarSkin.BLUE.getFileName();
        if (message.getCarSkin() != null) {
            image = message.getCarSkin().getFileName();
        }

        double posX = message.getPosX() > 0 ? message.getPosX() : START_X;
        double posY = resolveLaneY();

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
                + " en carril Y=" + posY);
        System.out.println("Remotos actuales: " + match.getRemotePlayers().size());
    }

    private double resolveLaneY() {
        boolean[] used = new boolean[LANES_Y.length];

        Player local = match.getLocalPlayer();
        if (local != null && local.getCar() != null) {
            markUsedLane(local.getCar().getY(), used);
        }

        for (Player remote : match.getRemotePlayers()) {
            if (remote != null && remote.getCar() != null) {
                markUsedLane(remote.getCar().getY(), used);
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