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

    /**
     * Crea una nueva instancia de {@code GameContext}.
     *
     * @param match valor del parametro {@code match}
     * @param engine valor del parametro {@code engine}
     * @param network valor del parametro {@code network}
     * @param obstacles valor del parametro {@code obstacles}
     * @param items valor del parametro {@code items}
     * @param peer valor del parametro {@code peer}
     * @param rulesManager valor del parametro {@code rulesManager}
     * @param resultManager valor del parametro {@code resultManager}
     */
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
     * Obtiene el valor de {@code Players}.
     *
     * @return valor de {@code Players}
     */
    public List<Player> getPlayers() {
        if (match == null || match.getPlayers() == null) {
            return Collections.emptyList();
        }
        return match.getPlayers();
    }

    /**
     * Obtiene el valor de {@code Cars}.
     *
     * @return valor de {@code Cars}
     */
    public List<Car> getCars() {
        return getPlayers().stream()
                .map(Player::getCar)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el valor de {@code Match}.
     *
     * @return valor de {@code Match}
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Obtiene el valor de {@code Engine}.
     *
     * @return valor de {@code Engine}
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Obtiene el valor de {@code Network}.
     *
     * @return valor de {@code Network}
     */
    public GameNetworkService getNetwork() {
        return network;
    }

    /**
     * Obtiene el valor de {@code Obstacles}.
     *
     * @return valor de {@code Obstacles}
     */
    public List<Obstacle> getObstacles() {
        return obstacles != null
                ? Collections.unmodifiableList(obstacles)
                : Collections.emptyList();
    }

    /**
     * Obtiene el valor de {@code Items}.
     *
     * @return valor de {@code Items}
     */
    public List<Item> getItems() {
        return items != null
                ? Collections.unmodifiableList(items)
                : Collections.emptyList();
    }

    /**
     * Obtiene el valor de {@code Peer}.
     *
     * @return valor de {@code Peer}
     */
    public UdpPeer getPeer() {
        return peer;
    }


    /**
     * Obtiene el valor de {@code RulesManager}.
     *
     * @return valor de {@code RulesManager}
     */
    public GameRulesManager getRulesManager() {
        return rulesManager;
    }

    /**
     * Obtiene el valor de {@code ResultManager}.
     *
     * @return valor de {@code ResultManager}
     */
    public GameResultManager getResultManager() {
        return resultManager;
    }
}
