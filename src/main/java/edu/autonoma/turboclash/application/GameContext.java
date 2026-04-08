package edu.autonoma.turboclash.application;

import edu.autonoma.turboclash.domain.services.GameEngine;
import edu.autonoma.turboclash.domain.model.Car;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.core.GameNetworkService;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameContext {

    private final Match match;
    private final GameEngine engine;
    private final GameNetworkService network;
    private final List<Obstacle> obstacles;
    private final List<Item> items;
    private final UdpPeer peer;

    public GameContext(Match match,
                       GameEngine engine,
                       GameNetworkService network,
                       List<Obstacle> obstacles,
                       List<Item> items,
                       UdpPeer peer) {
        this.match = match;
        this.engine = engine;
        this.network = network;
        this.obstacles = obstacles;
        this.items = items;
        this.peer = peer;
    }

    // =========================
    // 🔥 NUEVO: PLAYER LEVEL (IMPORTANTE)
    // =========================
    public List<Player> getPlayers() {
        if (match == null || match.getPlayers() == null) {
            return Collections.emptyList();
        }
        return match.getPlayers();
    }


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
}