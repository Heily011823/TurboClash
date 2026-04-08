package edu.autonoma.turboclash.domain.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa la responsabilidad de {@code Match} dentro del dominio del juego.
 */
public class Match {

    private Player localPlayer;
    private List<Player> remotePlayers;
    private int targetScore;

    private boolean finished = false;
    private Player winner;

    /**
     * Crea una nueva instancia de {@code Match}.
     *
     * @param localPlayer valor del parametro {@code localPlayer}
     * @param remotePlayers valor del parametro {@code remotePlayers}
     * @param targetScore valor del parametro {@code targetScore}
     */
    public Match(Player localPlayer, List<Player> remotePlayers, int targetScore) {
        this.localPlayer = localPlayer;
        this.remotePlayers = remotePlayers;
        this.targetScore = targetScore;
    }

    /**
     * Ejecuta la operacion {@code check}.
     */
    public void check() {
        if (finished) return;


        if (localPlayer.getCar().isFinishReached()) {
            this.finished = true;
            this.winner = localPlayer;
            return;
        }


        for (Player p : remotePlayers) {

            if (p.getCar().isFinishReached()) {
                this.finished = true;
                this.winner = p;
                return;
            }
        }
    }

    /**
     * Obtiene el valor de {@code Players}.
     *
     * @return valor de {@code Players}
     */
    public List<Player> getPlayers() {
        List<Player> all = new ArrayList<>();
        if (localPlayer != null) all.add(localPlayer);
        if (remotePlayers != null) all.addAll(remotePlayers);
        return all;
    }

    /**
     * Actualiza el valor de {@code Finished}.
     *
     * @param winner valor del parametro {@code winner}
     */
    public void setFinished(Player winner) {
        this.finished = true;
        this.winner = winner;
    }

    public Player getLocalPlayer() { return localPlayer; }
    public List<Player> getRemotePlayers() { return remotePlayers; }
    public boolean isFinished() { return finished; }
    public Player getWinner() { return winner; }

    /**
     * Ejecuta la operacion {@code addPlayer}.
     *
     * @param newPlayer valor del parametro {@code newPlayer}
     */
    public void addPlayer(Player newPlayer) {
        if (newPlayer == null) return;

        boolean exists = remotePlayers.stream()
                .anyMatch(p -> p.getId().equals(newPlayer.getId()));

        if (!exists) {
            remotePlayers.add(newPlayer);
        }
    }
}
