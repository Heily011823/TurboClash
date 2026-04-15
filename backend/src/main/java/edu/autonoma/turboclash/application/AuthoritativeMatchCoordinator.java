package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.ObstacleType;
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
import java.util.List;

/**
 * Coordinador autoritativo de la partida multijugador.
 *
 * <p>Esta clase administra la lógica principal del modelo host-autoritativo:
 * el host programa el inicio, actualiza el estado oficial del match,
 * determina el fin de la partida y distribuye snapshots al resto de clientes.</p>
 *
 * <p>Los clientes no-host aplican la información recibida por red,
 * pero no deben recalcular el resultado final ni sobrescribir
 * el movimiento local del jugador propio con el snapshot remoto.</p>
 *
 * <p><strong>Correcciones aplicadas:</strong></p>
 * <ul>
 *   <li>En {@code applySnapshot(...)} ya no se sobrescribe la posición del jugador local
 *       con el snapshot del host, evitando tirones, saltos o sensación de lag
 *       en clientes no-host.</li>
 *   <li>En {@code applyGameOver(...)} el ganador se busca correctamente por ID,
 *       evitando inconsistencias al resolver el jugador ganador.</li>
 * </ul>
 *
 * @author Valerie Moreno Castaño
 * @version 1.2
 * @since 2025-04-09
 */
public class AuthoritativeMatchCoordinator {

    /**
     * Retraso antes de iniciar oficialmente la partida una vez se cumplen
     * las condiciones mínimas de jugadores conectados.
     */
    private static final long START_DELAY_MS = 3000;

    /**
     * Duración máxima de la partida en milisegundos.
     */
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

    /**
     * Indica si el generador de objetos del mundo ya fue iniciado.
     */
    private boolean spawnerStarted;

    /**
     * Crea una nueva instancia del coordinador autoritativo.
     *
     * @param match estado de la partida
     * @param engine motor del juego
     * @param networkService servicio de red
     * @param rulesManager gestor de reglas
     * @param resultManager gestor de ranking y resultados
     * @param items lista compartida de ítems
     * @param obstacles lista compartida de obstáculos
     * @param spawner generador de objetos del mundo
     * @param localPort puerto local de esta instancia
     */
    public AuthoritativeMatchCoordinator(
            Match match,
            GameEngine engine,
            GameNetworkService networkService,
            GameRulesManager rulesManager,
            GameResultManager resultManager,
            List<Item> items,
            List<Obstacle> obstacles,
            GameSpawner spawner,
            int localPort
    ) {
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
     * Indica si esta instancia es actualmente el host autoritativo.
     *
     * @return true si el puerto local coincide con el host autoritativo
     */
    public boolean isLocalHost() {
        return match.getAuthoritativeHostPort() == localPort;
    }

    /**
     * Ejecuta la lógica autoritativa del host.
     *
     * <p>Solo el host debe:
     * iniciar la partida, actualizar el motor,
     * aplicar la meta visible y decidir el final del juego.</p>
     *
     * @param window ventana principal del juego
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
     * Inicia el match si ya se cumplió el tiempo programado de arranque.
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
     * Programa el inicio del match si hay suficientes jugadores conectados.
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

        networkService.sendGameStart(
                match.getLocalPlayer(),
                localPort,
                scheduledStartTime,
                match.getConnectedPlayerCount(),
                match.getMinPlayers(),
                match.getMaxPlayers()
        );
    }

    /**
     * Aplica el mensaje de inicio recibido desde el host.
     *
     * @param payload información del inicio sincronizado
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
     * Aplica un snapshot autoritativo recibido desde red.
     *
     * <p>Corrección clave:</p>
     * <ul>
     *   <li>Los jugadores remotos sí se actualizan completamente con el snapshot.</li>
     *   <li>El jugador local NO debe sobrescribir su posición local
     *       con el snapshot del host, porque eso produce saltos,
     *       temblores o movimiento bugueado en los clientes no-host.</li>
     * </ul>
     *
     * @param snapshot snapshot recibido desde el host
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

        Player localPlayer = match.getLocalPlayer();

        for (PlayerState state : snapshot.players) {

            if (match.isPlayerRemoved(state.playerId, state.playerName)) {
                continue;
            }

            boolean isLocalPlayer =
                    localPlayer != null &&
                            (
                                    (localPlayer.getId() != null && localPlayer.getId().equals(state.playerId)) ||
                                            (localPlayer.getName() != null
                                                    && state.playerName != null
                                                    && localPlayer.getName().equalsIgnoreCase(state.playerName))
                            );

            /*
             * No se debe sobrescribir la posición del jugador local con el snapshot
             * remoto del host, porque el cliente ya está aplicando su propio input
             * localmente. Si se hiciera, el carro "saltaría" o se sentiría bugueado.
             *
             * Sí se actualizan estados lógicos relevantes del jugador local:
             * score, vidas, eliminación, llegada a meta y ordenes finales.
             */
            if (isLocalPlayer) {
                localPlayer.setNetworkPort(state.port);
                localPlayer.setLastProcessedSequence(state.sequence);
                localPlayer.setScore(state.score);
                localPlayer.setFinishReached(state.finishReached);
                localPlayer.setEliminated(state.eliminated);
                localPlayer.setFinishOrder(state.finishOrder);
                localPlayer.setEliminationOrder(state.eliminationOrder);

                if (localPlayer.getCar() != null) {
                    localPlayer.getCar().setLives(state.lives);
                    localPlayer.getCar().setFinishReached(state.finishReached);
                }

                continue;
            }

            Player player = match.findPlayerByIdOrName(state.playerId, state.playerName);

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
     * Aplica el resultado final recibido desde el host.
     *
     * <p>Corrección incluida:
     * el ganador se busca correctamente por ID, no usando el ID también como nombre.</p>
     *
     * @param snapshot snapshot final del match
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

        Player winner = null;

        if (snapshot.winnerId != null) {
            winner = match.findPlayerByIdOrName(snapshot.winnerId, null);
        }

        if (winner == null && !ranking.isEmpty()) {
            winner = ranking.get(0);
        }

        match.finishGame(winner, ranking, snapshot.gameOverReason);
        match.setStarted(false);
        stopSpawner();
    }

    /**
     * Verifica si la partida debe terminar según el estado autoritativo.
     *
     * @param window ventana principal del juego
     */
    public void checkGameOver(GameWindow window) {
        long remainingMillis = Math.max(
                0L,
                match.getScheduledStartTime() + MATCH_DURATION_MS - System.currentTimeMillis()
        );

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

        if (!someoneReachedFinish
                && activePlayers > 1
                && remainingMillis > 0
                && (window == null || !window.isBackgroundFinished())) {
            return;
        }

        List<Player> ranking = resultManager.calculateRanking(match.getPlayers());
        Player winner = ranking.isEmpty() ? null : ranking.get(0);

        String reason =
                someoneReachedFinish ? "finish"
                        : remainingMillis <= 0 ? "timeout"
                          : (window != null && window.isBackgroundFinished()) ? "track_end"
                            : "elimination";

        match.finishGame(winner, ranking, reason);
        match.setStarted(false);
        stopSpawner();

        networkService.sendGameOver(match, items, obstacles, ranking, reason, localPort);
    }

    /**
     * Asegura que el spawner esté ejecutándose.
     */
    private void ensureSpawnerRunning() {
        if (!spawnerStarted) {
            spawner.start();
            spawnerStarted = true;
        }
    }

    /**
     * Detiene el spawner si estaba activo.
     */
    private void stopSpawner() {
        if (spawnerStarted) {
            spawner.stop();
            spawnerStarted = false;
        }
    }

    /**
     * Marca llegada a meta usando la posición visible de la meta en la vista.
     *
     * @param window ventana principal del juego
     */
    private void applyFinishByViewport(GameWindow window) {
        if (window == null || !window.isMetaVisible()) {
            return;
        }

        int metaX = window.getMetaX();

        for (Player player : match.getPlayers()) {

            if (player == null
                    || player.getCar() == null
                    || player.isFinishReached()
                    || player.isEliminated()) {
                continue;
            }

            int playerFront = (int) player.getCar().getX() + player.getCar().getWidth();

            if (playerFront >= metaX) {
                rulesManager.applyFinishBonus(player);
            }
        }
    }

    /**
     * Reemplaza el estado del mundo recibido desde red.
     *
     * @param itemStates lista de ítems serializados
     * @param obstacleStates lista de obstáculos serializados
     */
    private void replaceWorldState(
            List<WorldObjectState> itemStates,
            List<WorldObjectState> obstacleStates
    ) {
        replaceItems(itemStates);
        replaceObstacles(obstacleStates);
    }

    /**
     * Reconstruye un jugador a partir de un estado recibido por red.
     *
     * @param state estado del jugador
     * @return jugador reconstruido
     */
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

    /**
     * Reemplaza la lista actual de ítems por la recibida en el snapshot.
     *
     * @param itemStates estados de ítems
     */
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

    /**
     * Reemplaza la lista actual de obstáculos por la recibida en el snapshot.
     *
     * @param obstacleStates estados de obstáculos
     */
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