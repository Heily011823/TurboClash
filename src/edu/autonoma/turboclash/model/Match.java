package edu.autonoma.turboclash.model;

/**
 * Manages the game session between two players.
 * * Design Principles:
 * - SRP: Orchestrates match lifecycle and winner determination.
 * - Fail-Fast: Prevents logic errors like ending a match before it starts.
 */
public class Match {
    private final String id;
    private final Player localPlayer;
    private final Player remotePlayer;
    private final int targetScore;

    private long startTime;
    private long duration;
    private boolean finished;
    private Player winner;

    // Constants to avoid magic numbers
    private static final int INITIAL_TIME = 0;

    public Match(String id, Player localPlayer, Player remotePlayer, int targetScore) {
        this.id = id;
        this.localPlayer = localPlayer;
        this.remotePlayer = remotePlayer;
        this.targetScore = targetScore;
        this.finished = false;
        this.duration = INITIAL_TIME;
        this.startTime = INITIAL_TIME;
    }

    /**
     * Starts the match timer and resets state.
     */
    public void start() {
        this.finished = false;
        this.winner = null;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Ends the match and calculates final stats.
     * Refactoring: Prevents calculation if match never started.
     */
    public void end() {
        if (!this.finished && this.startTime != INITIAL_TIME) {
            this.finished = true;
            this.duration = System.currentTimeMillis() - this.startTime;
            determineWinner();
        }
    }

    /**
     * Checks if any player has reached the target score.
     * This adds the "Validation" layer you were missing.
     */
    public void checkWinCondition() {
        if (localPlayer.getScore().getPoints() >= targetScore ||
                remotePlayer.getScore().getPoints() >= targetScore) {
            end();
        }
    }

    public void determineWinner() {
        int localPoints = localPlayer.getScore().getPoints();
        int remotePoints = remotePlayer.getScore().getPoints();

        if (localPoints > remotePoints) {
            this.winner = localPlayer;
        } else if (remotePoints > localPoints) {
            this.winner = remotePlayer;
        } else {
            this.winner = null; // Tie
        }
    }

    // Getters
    public boolean isFinished() { return finished; }
    public String getId() { return id; }
    public Player getLocalPlayer() { return localPlayer; }
    public Player getRemotePlayer() { return remotePlayer; }
    public long getDuration() { return duration; }
    public int getTargetScore() { return targetScore; }
    public Player getWinner() { return winner; }
}