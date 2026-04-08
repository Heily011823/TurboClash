package edu.autonoma.turboclash.domain.model;

import java.util.ArrayList;
import java.util.List;

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
        this.remotePlayers = (remotePlayers != null) ? remotePlayers : new ArrayList<>();
        this.targetScore = targetScore;
    }

    /**
     * Ejecuta la operacion {@code check}.
     */
    public void check() {
        if (finished) {
            return;
        }

        if (localPlayer != null
                && localPlayer.getCar() != null
                && localPlayer.getCar().isFinishReached()) {
            this.finished = true;
            this.winner = localPlayer;
            return;
        }

        for (Player p : remotePlayers) {
            if (p != null
                    && p.getCar() != null
                    && p.getCar().isFinishReached()) {
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
        if (localPlayer != null) {
            all.add(localPlayer);
        }
        all.addAll(remotePlayers);
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

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public List<Player> getRemotePlayers() {
        return remotePlayers;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }

    public int getTargetScore() {
        return targetScore;
    }

    /**
     * Ejecuta la operacion {@code addPlayer}.
     *
     * @param newPlayer valor del parametro {@code newPlayer}
     */
    public void addPlayer(Player newPlayer) {
        if (newPlayer == null) {
            return;
        }

        if (localPlayer != null && samePlayer(localPlayer, newPlayer)) {
            return;
        }

        boolean exists = remotePlayers.stream()
                .anyMatch(existing -> samePlayer(existing, newPlayer));

        if (!exists) {
            remotePlayers.add(newPlayer);
            System.out.println("Jugador remoto agregado al Match: " + newPlayer.getName());
        }
    }

    public void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        remotePlayers.removeIf(existing -> samePlayer(existing, player));
    }

    public void removePlayerByName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return;
        }

        remotePlayers.removeIf(player ->
                player != null
                        && player.getName() != null
                        && player.getName().equalsIgnoreCase(playerName));
    }

    private boolean samePlayer(Player a, Player b) {
        if (a == null || b == null) {
            return false;
        }

        if (a.getId() != null && b.getId() != null) {
            return a.getId().equals(b.getId());
        }

        if (a.getName() != null && b.getName() != null) {
            return a.getName().equalsIgnoreCase(b.getName());
        }

        return false;
    }
}