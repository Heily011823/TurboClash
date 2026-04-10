package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Player;

public class NetworkSync {

    private static final long MOVEMENT_SEND_INTERVAL_MS = 50L;
    private static final long MOVEMENT_HEARTBEAT_MS = 250L;
    private static final long SNAPSHOT_SEND_INTERVAL_MS = 50L;

    private double lastX = Double.NaN;
    private double lastY = Double.NaN;
    private int lastScore = Integer.MIN_VALUE;
    private int lastLives = Integer.MIN_VALUE;
    private boolean lastFinishReached;
    private boolean lastEliminated;
    private long lastMovementSentAt;
    private long lastSnapshotSentAt;

    public void sync(GameContext context, Player player) {
        if (context == null || player == null || player.getCar() == null) {
            return;
        }

        long now = System.currentTimeMillis();
        boolean isHost = context.getCoordinator().isLocalHost();

        if (isHost && context.getMatch().isStarted() && now - lastSnapshotSentAt >= SNAPSHOT_SEND_INTERVAL_MS) {
            context.getNetwork().sendSnapshot(
                    context.getMatch(),
                    context.getEngine().getItems(),
                    context.getEngine().getObstacles(),
                    context.getPeer().getLocalPort()
            );
            lastSnapshotSentAt = now;
        }

        if (shouldSendMovement(player, now, isHost)) {
            context.getNetwork().sendMovement(player);
            rememberPlayerState(player, now);
        }
    }

    public void join(GameContext context, Player player) {
        if (context != null) {
            context.getNetwork().sendJoin(player);
        }
    }

    public void leave(GameContext context, Player player) {
        if (context != null) {
            context.getNetwork().sendLeave(player);
        }
    }

    private boolean shouldSendMovement(Player player, long now, boolean isHost) {
        if (isHost && !player.isEliminated() && !player.isFinishReached() && now - lastMovementSentAt < MOVEMENT_HEARTBEAT_MS) {
            return false;
        }

        double currentX = player.getCar().getX();
        double currentY = player.getCar().getY();
        int currentScore = player.getCurrentPoints();
        int currentLives = player.getLives();
        boolean currentFinishReached = player.isFinishReached();
        boolean currentEliminated = player.isEliminated();

        boolean changed = Double.compare(currentX, lastX) != 0
                || Double.compare(currentY, lastY) != 0
                || currentScore != lastScore
                || currentLives != lastLives
                || currentFinishReached != lastFinishReached
                || currentEliminated != lastEliminated;

        if (changed && now - lastMovementSentAt >= MOVEMENT_SEND_INTERVAL_MS) {
            return true;
        }

        return now - lastMovementSentAt >= MOVEMENT_HEARTBEAT_MS;
    }

    private void rememberPlayerState(Player player, long now) {
        lastX = player.getCar().getX();
        lastY = player.getCar().getY();
        lastScore = player.getCurrentPoints();
        lastLives = player.getLives();
        lastFinishReached = player.isFinishReached();
        lastEliminated = player.isEliminated();
        lastMovementSentAt = now;
    }
}
