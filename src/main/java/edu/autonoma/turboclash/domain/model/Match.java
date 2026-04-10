package edu.autonoma.turboclash.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa la responsabilidad de {@code Match} dentro del dominio del juego.
 */
public class Match {

    private Player localPlayer;
    private List<Player> remotePlayers;
    private int targetScore;

    private boolean finished = false;
    private boolean started = false;
    private Player winner;
    private long scheduledStartTime;
    private long remainingMillis = 180_000;
    private String gameOverReason;
    private final List<Player> ranking = new ArrayList<>();
    private final Map<String, Long> lastSequenceByPlayer = new ConcurrentHashMap<>();
    private final Set<String> removedPlayers = ConcurrentHashMap.newKeySet();
    private final Set<Integer> knownPorts = ConcurrentHashMap.newKeySet();
    private int minPlayers = 2;
    private int maxPlayers = 4;

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
        if (localPlayer != null && localPlayer.getNetworkPort() > 0) {
            knownPorts.add(localPlayer.getNetworkPort());
        }
    }

    /**
     * Ejecuta la operacion {@code check}.
     */
    public void check() {
        if (finished || started) {
            return;
        }
    }

    /**
     * Obtiene el valor de {@code Players}.
     *
     * @return valor de {@code Players}
     */
    public synchronized List<Player> getPlayers() {
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

    public synchronized void finishGame(Player winner, List<Player> ranking, String reason) {
        this.finished = true;
        this.winner = winner;
        this.gameOverReason = reason;
        this.ranking.clear();
        if (ranking != null) {
            this.ranking.addAll(ranking);
        }
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    public synchronized List<Player> getRemotePlayers() {
        return remotePlayers;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized void setStarted(boolean started) {
        this.started = started;
    }

    public synchronized long getScheduledStartTime() {
        return scheduledStartTime;
    }

    public synchronized void setScheduledStartTime(long scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public synchronized long getRemainingMillis() {
        return remainingMillis;
    }

    public synchronized void setRemainingMillis(long remainingMillis) {
        this.remainingMillis = Math.max(0, remainingMillis);
    }

    public synchronized String getGameOverReason() {
        return gameOverReason;
    }

    public synchronized List<Player> getRanking() {
        return Collections.unmodifiableList(new ArrayList<>(ranking));
    }

    public synchronized int getMinPlayers() {
        return minPlayers;
    }

    public synchronized int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTargetScore() {
        return targetScore;
    }

    /**
     * Agrega o actualiza un jugador remoto.
     *
     * @param newPlayer jugador remoto recibido por red
     */
    public synchronized void addPlayer(Player newPlayer) {
        if (newPlayer == null) {
            return;
        }

        if (isPlayerRemoved(newPlayer.getId(), newPlayer.getName())) {
            return;
        }

        if (localPlayer != null && samePlayer(localPlayer, newPlayer)) {
            return;
        }

        Player existing = findRemotePlayer(newPlayer.getId(), newPlayer.getName());

        if (existing != null) {
            syncPlayerData(existing, newPlayer);
            System.out.println("Jugador remoto actualizado en Match: " + existing.getName());
            return;
        }

        remotePlayers.add(newPlayer);
        if (newPlayer.getNetworkPort() > 0) {
            knownPorts.add(newPlayer.getNetworkPort());
        }
    }

    public synchronized void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        markPlayerRemoved(player.getId(), player.getName());
        remotePlayers.removeIf(existing -> samePlayer(existing, player));
    }

    public synchronized void removePlayerByName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return;
        }

        markPlayerRemoved(null, playerName);
        remotePlayers.removeIf(player ->
                player != null
                        && player.getName() != null
                        && player.getName().equalsIgnoreCase(playerName));
    }

    public synchronized Player findPlayerByIdOrName(String playerId, String playerName) {
        if (localPlayer != null) {
            boolean sameLocalById = localPlayer.getId() != null
                    && playerId != null
                    && localPlayer.getId().equals(playerId);

            boolean sameLocalByName = localPlayer.getName() != null
                    && playerName != null
                    && localPlayer.getName().equalsIgnoreCase(playerName);

            if (sameLocalById || sameLocalByName) {
                return localPlayer;
            }
        }

        return findRemotePlayer(playerId, playerName);
    }

    public synchronized Player findRemotePlayer(String playerId, String playerName) {
        for (Player player : remotePlayers) {
            if (player == null) {
                continue;
            }

            boolean sameId = player.getId() != null
                    && playerId != null
                    && player.getId().equals(playerId);

            boolean sameName = player.getName() != null
                    && playerName != null
                    && player.getName().equalsIgnoreCase(playerName);

            if (sameId || sameName) {
                return player;
            }
        }
        return null;
    }

    public synchronized boolean isPacketFresh(String playerId, String playerName, long sequence) {
        if (sequence <= 0) {
            return true;
        }

        String key = buildPlayerKey(playerId, playerName);
        long previous = lastSequenceByPlayer.getOrDefault(key, -1L);
        if (sequence <= previous) {
            return false;
        }

        lastSequenceByPlayer.put(key, sequence);
        return true;
    }

    public synchronized void markPlayerRemoved(String playerId, String playerName) {
        removedPlayers.add(buildPlayerKey(playerId, playerName));
    }

    public synchronized boolean isPlayerRemoved(String playerId, String playerName) {
        return removedPlayers.contains(buildPlayerKey(playerId, playerName));
    }

    public synchronized int getConnectedPlayerCount() {
        return getPlayers().size();
    }

    public synchronized void registerKnownPort(int port) {
        if (port > 0) {
            knownPorts.add(port);
        }
    }

    public synchronized int getHostPort() {
        int hostPort = Integer.MAX_VALUE;

        if (localPlayer != null && !isPlayerRemoved(localPlayer.getId(), localPlayer.getName())
                && localPlayer.getNetworkPort() > 0) {
            hostPort = Math.min(hostPort, localPlayer.getNetworkPort());
        }

        for (Player player : remotePlayers) {
            if (player == null || isPlayerRemoved(player.getId(), player.getName())) {
                continue;
            }

            if (player.getNetworkPort() > 0) {
                hostPort = Math.min(hostPort, player.getNetworkPort());
            }
        }

        for (Integer knownPort : knownPorts) {
            if (knownPort != null && knownPort > 0) {
                hostPort = Math.min(hostPort, knownPort);
            }
        }

        return hostPort == Integer.MAX_VALUE ? -1 : hostPort;
    }

    private void syncPlayerData(Player target, Player source) {
        if (target == null || source == null) {
            return;
        }

        if (target.getCar() != null && source.getCar() != null) {
            target.getCar().setPosition(source.getCar().getX(), source.getCar().getY());
            target.getCar().setLives(source.getCar().getLives());
            target.getCar().setFinishReached(source.getCar().isFinishReached());
        }

        target.setScore(source.getCurrentPoints());
        target.setFinishReached(source.isFinishReached());
        target.setEliminated(source.isEliminated());
        target.setFinishOrder(source.getFinishOrder());
        target.setEliminationOrder(source.getEliminationOrder());
        target.setHasScored(source.hasScored());
        target.setNetworkPort(source.getNetworkPort());
        target.setLastProcessedSequence(source.getLastProcessedSequence());
    }

    private String buildPlayerKey(String playerId, String playerName) {
        if (playerId != null && !playerId.isBlank()) {
            return "id:" + playerId.trim();
        }
        if (playerName != null && !playerName.isBlank()) {
            return "name:" + playerName.trim().toLowerCase();
        }
        return "unknown";
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
