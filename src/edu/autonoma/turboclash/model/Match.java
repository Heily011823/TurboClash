package edu.autonoma.turboclash.model;

public class Match {

    private final Player p1, p2;
    private final int targetScore;
    private boolean finished = false;
    private Player winner;

    public Match(Player p1, Player p2, int target) {
        this.p1 = p1;
        this.p2 = p2;
        this.targetScore = target;
    }

    public void check() {
        if (p1.getCurrentPoints() >= targetScore ||
                p2.getCurrentPoints() >= targetScore) {

            finished = true;

            if (p1.getCurrentPoints() > p2.getCurrentPoints()) {
                winner = p1;
            } else if (p2.getCurrentPoints() > p1.getCurrentPoints()) {
                winner = p2;
            } else {
                winner = null;
            }
        }
    }

    public boolean isFinished() { return finished; }
    public Player getWinner() { return winner; }
    public Player getLocalPlayer() { return p1; }
    public Player getRemotePlayer() { return p2; }
}