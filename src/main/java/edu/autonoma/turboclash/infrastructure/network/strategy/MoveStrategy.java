package edu.autonoma.turboclash.infrastructure.network.strategy;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;

/**
 * Representa la responsabilidad de {@code MoveStrategy} en las estrategias de mensajeria.
 */
public class MoveStrategy implements IMessageStrategy {

    private final Match match;

    /**
     * Crea una nueva instancia de {@code MoveStrategy}.
     *
     * @param match valor del parametro {@code match}
     */
    public MoveStrategy(Match match) {
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

        for (Player p : match.getRemotePlayers()) {
            if (p == null || p.getCar() == null) {
                continue;
            }

            boolean sameId =
                    p.getId() != null
                            && message.getPlayerId() != null
                            && p.getId().equals(message.getPlayerId());

            boolean sameName =
                    p.getName() != null
                            && message.getPlayerName() != null
                            && p.getName().equalsIgnoreCase(message.getPlayerName());

            if (sameId || sameName) {
                p.syncFromNetwork(
                        message.getPosX(),
                        message.getPosY(),
                        message.getScore()
                );

                System.out.println("Movimiento actualizado de "
                        + p.getName()
                        + " -> X: " + message.getPosX()
                        + ", Y: " + message.getPosY()
                        + ", Score: " + message.getScore());

                return;
            }
        }

        System.out.println("No se encontró jugador remoto para MOVEMENT: "
                + message.getPlayerName() + " / " + message.getPlayerId());
    }
}