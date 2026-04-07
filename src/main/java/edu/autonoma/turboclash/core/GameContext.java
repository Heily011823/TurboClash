package edu.autonoma.turboclash.core;

import edu.autonoma.turboclash.logic.GameEngine;
import edu.autonoma.turboclash.model.Car; // Importante añadir esto
import edu.autonoma.turboclash.model.Item;
import edu.autonoma.turboclash.model.Match;
import edu.autonoma.turboclash.model.Obstacle;
import edu.autonoma.turboclash.network.core.GameNetworkService;
import edu.autonoma.turboclash.network.core.UdpPeer;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * SOLID: Esta clase actúa como un DTO (Data Transfer Object) de contexto.
 * Su única responsabilidad (SRP) es proveer acceso al estado actual del juego.
 */
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

    // --- NUEVO MÉTODO PARA SOLUCIONAR EL ERROR ---
    /**
     * Obtiene la lista de carros extrayéndolos de los jugadores del Match.
     * Esto evita que GameApplication tenga que conocer la estructura interna de Match.
     */
    public List<Car> getCars() {
        if (match == null || match.getPlayers() == null) {
            return Collections.emptyList();
        }
        return match.getPlayers().stream()
                .map(player -> player.getCar())
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

    // Encapsulamiento: Devolvemos listas inmodificables para proteger el estado interno
    public List<Obstacle> getObstacles() {
        return obstacles != null ? Collections.unmodifiableList(obstacles) : Collections.emptyList();
    }

    public List<Item> getItems() {
        return items != null ? Collections.unmodifiableList(items) : Collections.emptyList();
    }

    public UdpPeer getPeer() {
        return peer;
    }
}