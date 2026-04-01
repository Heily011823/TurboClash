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

        if (localPlayer.getCurrentPoints() >= targetScore) {
            finished = true;
            winner = localPlayer;
            return;
        }

        for (Player p : remotePlayers) {
            if (p.getCurrentPoints() >= targetScore) {
                finished = true;
                winner = p;
                return;
            }
        }
    }

    public List<Player> getPlayers() {
        List<Player> all = new ArrayList<>();
        all.add(localPlayer);
        all.addAll(remotePlayers);
        return all;
    }

    public Player getLocalPlayer() { return localPlayer; }
    public List<Player> getRemotePlayers() { return remotePlayers; }
    public boolean isFinished() { return finished; }
    public Player getWinner() { return winner; }
}