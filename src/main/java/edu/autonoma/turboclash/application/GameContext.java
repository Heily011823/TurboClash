package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.services.GameEngine;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.domain.model.*;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Agrupa el contexto compartido de {@code GameContext} en la capa de aplicacion.
 */
public class GameContext {

    private final Match match;
    private final GameEngine engine;
    private final GameNetworkService network;
    private final List<Obstacle> obstacles;
    private final List<Item> items;
    private final UdpPeer peer;

    private final GameRulesManager rulesManager;
    private final GameResultManager resultManager;

    private Player localPlayer;

    public GameContext(Match match,
                       GameEngine engine,
                       GameNetworkService network,
                       List<Obstacle> obstacles,
                       List<Item> items,
                       UdpPeer peer,
                       GameRulesManager rulesManager,
                       GameResultManager resultManager) {

        this.match = match;
        this.engine = engine;
        this.network = network;
        this.obstacles = obstacles;
        this.items = items;
        this.peer = peer;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;
    }

    /**
     * Obtiene la lista de jugadores.
     */
    public List<Player> getPlayers() {
        if (match == null || match.getPlayers() == null) {
            return Collections.emptyList();
        }
        return match.getPlayers();
    }

    /**
     * Obtiene la lista de autos de los jugadores.
     */
    public List<Car> getCars() {
        return getPlayers().stream()
                .map(Player::getCar)
                .collect(Collectors.toList());
    }

    public Match getMatch() {
        return match;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public GameNetworkService getNetwork() {
        return network;
    }

    public List<Obstacle> getObstacles() {
        return obstacles != null
                ? Collections.unmodifiableList(obstacles)
                : Collections.emptyList();
    }

    public List<Item> getItems() {
        return items != null
                ? Collections.unmodifiableList(items)
                : Collections.emptyList();
    }

    public UdpPeer getPeer() {
        return peer;
    }

    public GameRulesManager getRulesManager() {
        return rulesManager;
    }

    public GameResultManager getResultManager() {
        return resultManager;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    /**
     * Agrega un jugador al match correctamente usando Match.addPlayer()
     */
    public void addPlayer(Player player) {
        if (match != null) {
            match.addPlayer(player);
        }
    }
}