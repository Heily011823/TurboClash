package edu.autonoma.turboclash.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representa una partida multijugador de TurboClash.
 *
 * <p>La clase Match centraliza el estado compartido de la partida:
 * jugadores, inicio, finalización, ranking, puertos conocidos y
 * selección del host autoritativo.</p>
 *
 * <p><strong>Corrección aplicada:</strong>
 * el host autoritativo ya no se fija únicamente con el primer puerto
 * recibido. Ahora converge siempre hacia el menor puerto válido conocido,
 * evitando que distintos clientes queden bloqueados con hosts diferentes
 * y terminen aceptando o ignorando mensajes críticos como
 * {@code GAME_START}, {@code SYNC} o {@code GAME_OVER}.</p>
 *
 * @author Elizabeth Meneses Muñoz
 * @version 1.1
 * @since 2025-04-09
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

    /**
     * Puerto del host autoritativo.
     *
     * <p>Antes se fijaba una sola vez y no cambiaba.
     * Ahora se mantiene consistente con el menor puerto válido conocido.</p>
     */
    private Integer authoritativeHostPort;

    private int minPlayers = 2;
    private int maxPlayers = 4;

    /**
     * Crea una nueva instancia de Match.
     *
     * @param localPlayer jugador local
     * @param remotePlayers lista inicial de jugadores remotos
     * @param targetScore puntaje objetivo de la partida
     */
    public Match(Player localPlayer, List<Player> remotePlayers, int targetScore) {
        this.localPlayer = localPlayer;
        this.remotePlayers = (remotePlayers != null) ? remotePlayers : new ArrayList<>();
        this.targetScore = targetScore;

        if (localPlayer != null && localPlayer.getNetworkPort() > 0) {
            knownPorts.add(localPlayer.getNetworkPort());
        }

        // Asegura consistencia inicial del host.
        recalculateAuthoritativeHostPort();
    }

    /**
     * Método de chequeo general.
     */
    public void check() {
        if (finished || started) {
            return;
        }
    }

    /**
     * Retorna todos los jugadores de la partida.
     *
     * @return lista con el jugador local y los remotos
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
     * Marca la partida como finalizada.
     *
     * @param winner jugador ganador
     */
    public void setFinished(Player winner) {
        this.finished = true;
        this.winner = winner;
    }

    /**
     * Finaliza la partida con ganador, ranking y motivo.
     *
     * @param winner jugador ganador
     * @param ranking ranking final
     * @param reason motivo del final de partida
     */
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
        if (localPlayer != null && localPlayer.getNetworkPort() > 0) {
            knownPorts.add(localPlayer.getNetworkPort());
        }
        recalculateAuthoritativeHostPort();
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
     * <p>Si el jugador ya existe, sincroniza sus datos.
     * Si es nuevo, lo agrega y registra su puerto.</p>
     *
     * @param newPlayer jugador a agregar o actualizar
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

            if (newPlayer.getNetworkPort() > 0) {
                knownPorts.add(newPlayer.getNetworkPort());
                recalculateAuthoritativeHostPort();
            }
            return;
        }

        remotePlayers.add(newPlayer);
        if (newPlayer.getNetworkPort() > 0) {
            knownPorts.add(newPlayer.getNetworkPort());
        }

        recalculateAuthoritativeHostPort();
    }

    /**
     * Elimina un jugador remoto y recalcula el host autoritativo.
     *
     * @param player jugador a remover
     */
    public synchronized void removePlayer(Player player) {
        if (player == null) {
            return;
        }

        markPlayerRemoved(player.getId(), player.getName());
        remotePlayers.removeIf(existing -> samePlayer(existing, player));

        recalculateAuthoritativeHostPort();
    }

    /**
     * Elimina un jugador por nombre y recalcula el host autoritativo.
     *
     * @param playerName nombre del jugador a remover
     */
    public synchronized void removePlayerByName(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return;
        }

        markPlayerRemoved(null, playerName);
        remotePlayers.removeIf(player ->
                player != null
                        && player.getName() != null
                        && player.getName().equalsIgnoreCase(playerName));

        recalculateAuthoritativeHostPort();
    }

    /**
     * Busca un jugador por id o nombre.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     * @return jugador encontrado o null
     */
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

    /**
     * Busca un jugador remoto por id o nombre.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     * @return jugador remoto encontrado o null
     */
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

    /**
     * Verifica si un paquete es fresco según la secuencia recibida.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     * @param sequence número de secuencia
     * @return true si el paquete debe procesarse
     */
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

    /**
     * Marca un jugador como removido.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     */
    public synchronized void markPlayerRemoved(String playerId, String playerName) {
        removedPlayers.add(buildPlayerKey(playerId, playerName));
    }

    /**
     * Verifica si un jugador ya fue removido.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     * @return true si fue removido
     */
    public synchronized boolean isPlayerRemoved(String playerId, String playerName) {
        return removedPlayers.contains(buildPlayerKey(playerId, playerName));
    }

    /**
     * Retorna la cantidad de jugadores conectados.
     *
     * @return número total de jugadores
     */
    public synchronized int getConnectedPlayerCount() {
        return getPlayers().size();
    }

    /**
     * Registra un puerto conocido y actualiza el host autoritativo si corresponde.
     *
     * @param port puerto a registrar
     */
    public synchronized void registerKnownPort(int port) {
        if (port > 0) {
            knownPorts.add(port);
            recalculateAuthoritativeHostPort();
        }
    }

    /**
     * Obtiene el menor puerto válido disponible entre jugador local,
     * remotos y puertos conocidos.
     *
     * @return menor puerto válido o -1 si no hay ninguno
     */
    public synchronized int getHostPort() {
        int hostPort = Integer.MAX_VALUE;

        if (localPlayer != null
                && !isPlayerRemoved(localPlayer.getId(), localPlayer.getName())
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

    /**
     * Define o ajusta el host autoritativo.
     *
     * <p><strong>Corrección clave:</strong>
     * este método ya no deja bloqueado el primer puerto que llega.
     * En su lugar, actualiza el host si aparece un puerto menor,
     * garantizando convergencia entre clientes.</p>
     *
     * @param port puerto candidato a host autoritativo
     */
    public synchronized void lockAuthoritativeHostPort(int port) {
        if (port <= 0) {
            return;
        }

        knownPorts.add(port);

        if (authoritativeHostPort == null || port < authoritativeHostPort) {
            authoritativeHostPort = port;
        }
    }

    /**
     * Retorna el puerto actual del host autoritativo.
     *
     * @return puerto del host autoritativo o -1 si no existe
     */
    public synchronized int getAuthoritativeHostPort() {
        if (authoritativeHostPort != null && authoritativeHostPort > 0) {
            return authoritativeHostPort;
        }
        return getHostPort();
    }

    /**
     * Recalcula el host autoritativo usando la regla del menor puerto válido.
     *
     * <p>Este método ayuda a mantener consistencia cuando:
     * se agrega un jugador, se elimina uno, cambia el jugador local
     * o se descubren nuevos peers.</p>
     */
    public synchronized void recalculateAuthoritativeHostPort() {
        int candidate = getHostPort();
        authoritativeHostPort = (candidate > 0) ? candidate : null;
    }

    /**
     * Sincroniza los datos relevantes de un jugador origen hacia un jugador destino.
     *
     * @param target jugador destino
     * @param source jugador origen
     */
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

    /**
     * Construye una clave estable para identificar jugadores.
     *
     * @param playerId id del jugador
     * @param playerName nombre del jugador
     * @return clave única
     */
    private String buildPlayerKey(String playerId, String playerName) {
        if (playerId != null && !playerId.isBlank()) {
            return "id:" + playerId.trim();
        }
        if (playerName != null && !playerName.isBlank()) {
            return "name:" + playerName.trim().toLowerCase();
        }
        return "unknown";
    }

    /**
     * Determina si dos referencias representan al mismo jugador.
     *
     * @param a jugador A
     * @param b jugador B
     * @return true si son el mismo jugador
     */
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