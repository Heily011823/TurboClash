package edu.autonoma.turboclash.model;

import java.util.List;
import java.util.ArrayList;

public class Match {

    private Player localPlayer;
    private List<Player> remotePlayers;
    private int targetScore;

    private boolean finished = false;
    private Player winner;

    public Match(Player localPlayer, List<Player> remotePlayers, int targetScore) {
        this.localPlayer = localPlayer;
        this.remotePlayers = remotePlayers;
        this.targetScore = targetScore;
    }

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

    public List<Player> getPlayers() {
        List<Player> all = new ArrayList<>();
        if (localPlayer != null) all.add(localPlayer);
        if (remotePlayers != null) all.addAll(remotePlayers);
        return all;
    }

    public void setFinished(Player winner) {
        this.finished = true;
        this.winner = winner;
    }

    public Player getLocalPlayer() { return localPlayer; }
    public List<Player> getRemotePlayers() { return remotePlayers; }
    public boolean isFinished() { return finished; }
    public Player getWinner() { return winner; }
}