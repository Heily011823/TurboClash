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
 * Coordina la lógica autoritativa de una partida multijugador.
 *
 * <p>Responsabilidades principales:</p>
 * <ul>
 *   <li>Determinar si la instancia local actúa como host autoritativo.</li>
 *   <li>Programar y arrancar la partida cuando se cumplan las condiciones.</li>
 *   <li>Actualizar el estado global del juego desde el host.</li>
 *   <li>Aplicar snapshots recibidos desde red en clientes no host.</li>
 *   <li>Calcular y propagar el resultado final del match.</li>
 * </ul>
 *
 * <p>En el modelo actual, el host autoritativo es el único que debe decidir
 * el ranking y el ganador final. Los clientes solo deben aplicar y mostrar
 * el resultado recibido por red.</p>
 *
 * @author Valerie Moreno Castaño
 * @version 1.1
 * @since 2025-04-09
 */
public class AuthoritativeMatchCoordinator {

    /**
     * Retraso entre que se detectan suficientes jugadores y el inicio real del match.
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
     * Construye un nuevo coordinador autoritativo del match.
     *
     * @param match partida actual
     * @param engine motor principal del juego
     * @param networkService servicio de red del juego
     * @param rulesManager gestor de reglas
     * @param resultManager gestor de resultados y ranking
     * @param items lista compartida de ítems del mundo
     * @param obstacles lista compartida de obstáculos del mundo
     * @param spawner generador de objetos del mundo
     * @param localPort puerto UDP local de esta instancia
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
     * Indica si la instancia local es actualmente el host autoritativo.
     *
     * @return true si el puerto local coincide con el host autoritativo del match
     */
    public boolean isLocalHost() {
        return match.getAuthoritativeHostPort() == localPort;
    }

    /**
     * Ejecuta la actualización autoritativa del match.
     *
     * <p>Solo el host autoritativo debe:
     * programar el inicio, arrancar la partida, actualizar el motor,
     * aplicar la llegada a meta por viewport y decidir el final del juego.</p>
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
     * Inicia la partida si el tiempo programado ya fue alcanzado.
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
     * Programa el inicio de la partida cuando hay suficientes jugadores.
     *
     * <p>Solo debe hacerlo el host autoritativo.</p>
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
     * Aplica la programación de inicio recibida desde red.
     *
     * @param payload datos del inicio sincronizado
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
     * Aplica un snapshot autoritativo recibido desde el host.
     *
     * <p>Actualiza:
     * jugadores, puertos, estado del match, tiempo restante y objetos del mundo.</p>
     *
     * @param snapshot instantánea del estado del match
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

            if (match.isPlayerRemoved(state.playerId, state.playerName)) {
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
     * Aplica el resultado final recibido desde el host autoritativo.
     *
     * <p>Corrección aplicada:</p>
     * <ul>
     *   <li>El ganador se busca correctamente por su ID real.</li>
     *   <li>Ya no se intenta buscar usando el ID también como nombre.</li>
     *   <li>Si no se encuentra al ganador explícito, se usa el primer jugador
     *       del ranking recibido como fallback consistente.</li>
     * </ul>
     *
     * @param snapshot snapshot final con el resultado del match
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

        /*
         * Corrección:
         * antes se buscaba con (winnerId, winnerId), usando el ID también
         * como nombre. Eso podía provocar que el ganador no se resolviera
         * correctamente en algunos clientes.
         */
        Player winner = null;

        if (snapshot.winnerId != null) {
            winner = match.findPlayerByIdOrName(snapshot.winnerId, null);
        }

        /*
         * Si por alguna razón el ganador no pudo resolverse por ID,
         * se toma el primer jugador del ranking recibido.
         */
        if (winner == null && !ranking.isEmpty()) {
            winner = ranking.get(0);
        }

        match.finishGame(winner, ranking, snapshot.gameOverReason);
        match.setStarted(false);
        stopSpawner();
    }

    /**
     * Verifica si el match debe finalizar.
     *
     * <p>Solo debe ejecutarse en el host autoritativo.</p>
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
     * Asegura que el spawner esté activo.
     */
    private void ensureSpawnerRunning() {
        if (!spawnerStarted) {
            spawner.start();
            spawnerStarted = true;
        }
    }

    /**
     * Detiene el spawner si está activo.
     */
    private void stopSpawner() {
        if (spawnerStarted) {
            spawner.stop();
            spawnerStarted = false;
        }
    }

    /**
     * Marca jugadores como llegados a la meta cuando el frente del carro
     * alcanza la posición X de la meta visible.
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
     * Reemplaza el estado completo de objetos del mundo.
     *
     * @param itemStates estado de ítems
     * @param obstacleStates estado de obstáculos
     */
    private void replaceWorldState(
            List<WorldObjectState> itemStates,
            List<WorldObjectState> obstacleStates
    ) {
        replaceItems(itemStates);
        replaceObstacles(obstacleStates);
    }

    /**
     * Construye un jugador a partir de un estado recibido por red.
     *
     * @param state estado serializado del jugador
     * @return nuevo jugador reconstruido
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
     * @param itemStates estados de ítems del mundo
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
     * @param obstacleStates estados de obstáculos del mundo
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