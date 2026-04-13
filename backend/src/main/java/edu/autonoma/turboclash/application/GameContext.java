package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.domain.services.GameEngine;
import edu.autonoma.turboclash.domain.services.GameResultManager;
import edu.autonoma.turboclash.domain.services.GameRulesManager;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa la clase `GameContext` y define su responsabilidad dentro del sistema.
 * @author Heily Rios Ayala </heily.riosa@autonoma.edu.co>
 * @version 1.0
 * @since 2025-04-09
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
    private final AuthoritativeMatchCoordinator coordinator;

    private Player localPlayer;

    /**
     * Crea una nueva instancia de `GameContext`.
     * @param match valor del parametro `match`
     * @param engine valor del parametro `engine`
     * @param network valor del parametro `network`
     * @param obstacles valor del parametro `obstacles`
     * @param items valor del parametro `items`
     * @param peer valor del parametro `peer`
     * @param rulesManager valor del parametro `rulesManager`
     * @param resultManager valor del parametro `resultManager`
     * @param coordinator valor del parametro `coordinator`
     */
    public GameContext(Match match,
                       GameEngine engine,
                       GameNetworkService network,
                       List<Obstacle> obstacles,
                       List<Item> items,
                       UdpPeer peer,
                       GameRulesManager rulesManager,
                       GameResultManager resultManager,
                       AuthoritativeMatchCoordinator coordinator) {

        this.match = match;
        this.engine = engine;
        this.network = network;
        this.obstacles = obstacles;
        this.items = items;
        this.peer = peer;
        this.rulesManager = rulesManager;
        this.resultManager = resultManager;
        this.coordinator = coordinator;
    }

    /**
     * Obtiene el valor asociado a `getPlayers`.
     * @return resultado de la operacion documentada
     */
    public List<Player> getPlayers() {
        if (match == null || match.getPlayers() == null) {
            return Collections.emptyList();
        }
        return match.getPlayers();
    }

    /**
     * Obtiene el valor asociado a `getCars`.
     * @return resultado de la operacion documentada
     */
    public List<Car> getCars() {
        return getPlayers().stream()
                .map(Player::getCar)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el valor asociado a `getMatch`.
     * @return resultado de la operacion documentada
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Obtiene el valor asociado a `getEngine`.
     * @return resultado de la operacion documentada
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Obtiene el valor asociado a `getNetwork`.
     * @return resultado de la operacion documentada
     */
    public GameNetworkService getNetwork() {
        return network;
    }

    /**
     * Obtiene el valor asociado a `getObstacles`.
     * @return resultado de la operacion documentada
     */
    public List<Obstacle> getObstacles() {
        return obstacles != null
                ? Collections.unmodifiableList(obstacles)
                : Collections.emptyList();
    }

    /**
     * Obtiene el valor asociado a `getItems`.
     * @return resultado de la operacion documentada
     */
    public List<Item> getItems() {
        return items != null
                ? Collections.unmodifiableList(items)
                : Collections.emptyList();
    }

    /**
     * Obtiene el valor asociado a `getPeer`.
     * @return resultado de la operacion documentada
     */
    public UdpPeer getPeer() {
        return peer;
    }

    /**
     * Obtiene el valor asociado a `getRulesManager`.
     * @return resultado de la operacion documentada
     */
    public GameRulesManager getRulesManager() {
        return rulesManager;
    }

    /**
     * Obtiene el valor asociado a `getResultManager`.
     * @return resultado de la operacion documentada
     */
    public GameResultManager getResultManager() {
        return resultManager;
    }

    /**
     * Obtiene el valor asociado a `getCoordinator`.
     * @return resultado de la operacion documentada
     */
    public AuthoritativeMatchCoordinator getCoordinator() {
        return coordinator;
    }

    /**
     * Obtiene el valor asociado a `getLocalPlayer`.
     * @return resultado de la operacion documentada
     */
    public Player getLocalPlayer() {
        return localPlayer;
    }

    /**
     * Actualiza el valor asociado a `setLocalPlayer`.
     * @param localPlayer valor del parametro `localPlayer`
     */
    public void setLocalPlayer(Player localPlayer) {
        this.localPlayer = localPlayer;
    }

    /**
     * Agrega el elemento necesario para add player.
     * @param player valor del parametro `player`
     */
    public void addPlayer(Player player) {
        if (match != null && player != null) {
            match.addPlayer(player);
        }
    }
}
