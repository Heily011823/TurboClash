package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.ObstacleType;
import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameEngine;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameSpawner;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.message.GameStartPayload;
import edu.autonoma.turboclash.infrastructure.network.message.MatchSnapshot;
import edu.autonoma.turboclash.infrastructure.network.message.PlayerState;
import edu.autonoma.turboclash.infrastructure.network.message.WorldObjectState;
import edu.autonoma.turboclash.presentation.view.GameWindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representa la clase `AuthoritativeMatchCoordinator` y define su responsabilidad dentro del sistema.
 *@author Valerie Moreno Castaño</valerie.morenoc@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
 */
public class AuthoritativeMatchCoordinator {

    private static final long START_DELAY_MS = 3000;
    private static final long MATCH_DURATION_MS = 180_000;

    private final Match match;
    private final GameEngine engine;
    private final GameNetworkService networkService;
    private final GameRulesManager rulesManager;
    private final GameResultManager resultManager;
    private final List<Item> items;
    private final List<Obstacle> obstacles;
    private final GameSpawner spawner;
    private final int localPort;
    private boolean spawnerStarted;

    /**
     * Crea una nueva instancia de `AuthoritativeMatchCoordinator`.
     * @param match valor del parametro `match`
     * @param engine valor del parametro `engine`
     * @param networkService valor del parametro `networkService`
     * @param rulesManager valor del parametro `rulesManager`
     * @param resultManager valor del parametro `resultManager`
     * @param items valor del parametro `items`
     * @param obstacles valor del parametro `obstacles`
     * @param spawner valor del parametro `spawner`
     * @param localPort valor del parametro `localPort`
     */
    public AuthoritativeMatchCoordinator(Match match,
                                         GameEngine engine,
                                         GameNetworkService networkService,
                                         GameRulesManager rulesManager,
                                         GameResultManager resultManager,
                                         List<Item> items,
                                         List<Obstacle> obstacles,
                                         GameSpawner spawner,
                                         int localPort) {
        this.match = match;
        this.engine = engine;
        this.networkService = networkService;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;
        this.items = items;
        this.obstacles = obstacles;
        this.spawner = spawner;
        this.localPort = localPort;
    }

    /**
     * Indica la condicion evaluada por `isLocalHost`.
     * @return resultado de la operacion documentada
     */
    public boolean isLocalHost() {
        return match.getAuthoritativeHostPort() == localPort;
    }

    /**
     * Actualiza el estado relacionado con update host authority.
     * @param window valor del parametro `window`
     */
    public void updateHostAuthority(GameWindow window) {
        if (!isLocalHost() || match.isFinished()) {
            return;
        }

        if (!match.isStarted()) {
            if (match.getConnectedPlayerCount() < match.getMinPlayers()) {
                match.setScheduledStartTime(0L);
                stopSpawner();
                return;
            }
            maybeScheduleGameStart();
            maybeStartMatch();
            return;
        }

        ensureSpawnerRunning();
        engine.update();
        applyFinishByViewport(window);
        checkGameOver(window);
    }

    /**
     * Ejecuta la operacion publica `maybeStartMatch`.
     */
    public void maybeStartMatch() {
        long scheduledStartTime = match.getScheduledStartTime();
        if (match.isStarted() || scheduledStartTime <= 0L) {
            return;
        }

        if (System.currentTimeMillis() >= scheduledStartTime) {
            match.lockAuthoritativeHostPort(localPort);
            match.setStarted(true);
            match.setRemainingMillis(MATCH_DURATION_MS);
            ensureSpawnerRunning();
        }
    }

    /**
     * Ejecuta la operacion publica `maybeScheduleGameStart`.
     */
    public void maybeScheduleGameStart() {
        if (!isLocalHost() || match.isStarted() || match.getScheduledStartTime() > 0) {
            return;
        }

        if (match.getConnectedPlayerCount() < match.getMinPlayers()) {
            return;
        }

        long scheduledStartTime = System.currentTimeMillis() + START_DELAY_MS;
        match.lockAuthoritativeHostPort(localPort);
        match.setScheduledStartTime(scheduledStartTime);
        networkService.sendGameStart(match.getLocalPlayer(), localPort, scheduledStartTime,
                match.getConnectedPlayerCount(), match.getMinPlayers(), match.getMaxPlayers());
    }

    /**
     * Aplica la logica correspondiente a apply game start.
     * @param payload valor del parametro `payload`
     */
    public void applyGameStart(GameStartPayload payload) {
        if (payload == null) {
            return;
        }

        match.lockAuthoritativeHostPort(payload.hostPort);
        match.registerKnownPort(payload.hostPort);
        match.setScheduledStartTime(payload.scheduledStartTime);
    }

    /**
     * Aplica la logica correspondiente a apply snapshot.
     * @param snapshot valor del parametro `snapshot`
     */
    public void applySnapshot(MatchSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }

        match.lockAuthoritativeHostPort(snapshot.hostPort);
        match.registerKnownPort(snapshot.hostPort);
        match.setScheduledStartTime(snapshot.scheduledStartTime);
        match.setStarted(snapshot.started);
        match.setRemainingMillis(snapshot.remainingMillis);

        for (PlayerState state : snapshot.players) {
            Player player = match.findPlayerByIdOrName(state.playerId, state.playerName);
            if (match.isPlayerRemoved(state.playerId, state.playerName)) {
                continue;
            }

            if (player == null) {
                player = createPlayerFromState(state);
                match.addPlayer(player);
            }

            player.setNetworkPort(state.port);
            player.setLastProcessedSequence(state.sequence);
            player.syncFromNetwork(
                    state.posX,
                    state.posY,
                    state.score,
                    state.lives,
                    state.finishReached,
                    state.eliminated,
                    state.finishOrder,
                    state.eliminationOrder
            );
        }

        replaceWorldState(snapshot.items, snapshot.obstacles);

        if (snapshot.finished) {
            applyGameOver(snapshot);
        }
    }

    /**
     * Aplica la logica correspondiente a apply game over.
     * @param snapshot valor del parametro `snapshot`
     */
    public void applyGameOver(MatchSnapshot snapshot) {
        if (snapshot == null) {
            return;
        }

        match.lockAuthoritativeHostPort(snapshot.hostPort);
        List<Player> ranking = new ArrayList<>();
        for (PlayerState state : snapshot.players) {
            Player player = match.findPlayerByIdOrName(state.playerId, state.playerName);
            if (player == null) {
                player = createPlayerFromState(state);
                match.addPlayer(player);
            }

            player.syncFromNetwork(
                    state.posX,
                    state.posY,
                    state.score,
                    state.lives,
                    state.finishReached,
                    state.eliminated,
                    state.finishOrder,
                    state.eliminationOrder
            );
            ranking.add(player);
        }

        Player winner = snapshot.winnerId != null
                ? match.findPlayerByIdOrName(snapshot.winnerId, snapshot.winnerId)
                : null;

        if (winner == null && !ranking.isEmpty()) {
            winner = ranking.get(0);
        }

        match.finishGame(winner, ranking, snapshot.gameOverReason);
        match.setStarted(false);
        stopSpawner();
    }

    /**
     * Ejecuta la operacion publica `checkGameOver`.
     * @param window valor del parametro `window`
     */
    public void checkGameOver(GameWindow window) {
        long remainingMillis = Math.max(0L, match.getScheduledStartTime() + MATCH_DURATION_MS - System.currentTimeMillis());
        match.setRemainingMillis(remainingMillis);

        boolean someoneReachedFinish = false;
        int activePlayers = 0;

        for (Player player : match.getPlayers()) {
            if (player == null) {
                continue;
            }

            if (player.isFinishReached()) {
                someoneReachedFinish = true;
            }

            if (player.isAlive() && !player.isEliminated()) {
                activePlayers++;
            }
        }

        if (!someoneReachedFinish && activePlayers > 1 && remainingMillis > 0 && (window == null || !window.isBackgroundFinished())) {
            return;
        }

        List<Player> ranking = resultManager.calculateRanking(match.getPlayers());
        Player winner = ranking.isEmpty() ? null : ranking.get(0);
        String reason = someoneReachedFinish ? "finish" :
                remainingMillis <= 0 ? "timeout" :
                (window != null && window.isBackgroundFinished()) ? "track_end" :
                        "elimination";

        match.finishGame(winner, ranking, reason);
        match.setStarted(false);
        stopSpawner();
        networkService.sendGameOver(match, items, obstacles, ranking, reason, localPort);
    }

    private void ensureSpawnerRunning() {
        if (!spawnerStarted) {
            spawner.start();
            spawnerStarted = true;
        }
    }

    private void stopSpawner() {
        if (spawnerStarted) {
            spawner.stop();
            spawnerStarted = false;
        }
    }

    private void applyFinishByViewport(GameWindow window) {
        if (window == null || !window.isMetaVisible()) {
            return;
        }

        int metaX = window.getMetaX();
        for (Player player : match.getPlayers()) {
            if (player == null || player.getCar() == null || player.isFinishReached() || player.isEliminated()) {
                continue;
            }

            int playerFront = (int) player.getCar().getX() + player.getCar().getWidth();
            if (playerFront >= metaX) {
                rulesManager.applyFinishBonus(player);
            }
        }
    }

    private void replaceWorldState(List<WorldObjectState> itemStates, List<WorldObjectState> obstacleStates) {
        replaceItems(itemStates);
        replaceObstacles(obstacleStates);
    }

    private Player createPlayerFromState(PlayerState state) {
        Car car = new Car(
                state.playerId != null ? state.playerId : state.playerName,
                state.posX,
                state.posY,
                100,
                50,
                "/image/Car_Blue.png"
        );
        Player player = new Player(state.playerId, state.playerName, car);
        player.setNetworkPort(state.port);
        return player;
    }

    private void replaceItems(List<WorldObjectState> itemStates) {
        items.clear();
        if (itemStates == null) {
            return;
        }

        for (WorldObjectState state : itemStates) {
            Item item = new Item(state.id, state.posX, state.posY, state.width, state.height);
            item.setVisible(state.visible);
            items.add(item);
        }
    }

    private void replaceObstacles(List<WorldObjectState> obstacleStates) {
        obstacles.clear();
        if (obstacleStates == null) {
            return;
        }

        for (WorldObjectState state : obstacleStates) {
            Obstacle obstacle = new Obstacle(
                    state.id,
                    state.posX,
                    state.posY,
                    state.width,
                    state.height,
                    ObstacleType.valueOf(state.type)
            );
            obstacle.setVisible(state.visible);
            obstacle.setProcessed(state.processed);
            obstacles.add(obstacle);
        }
    }
}
