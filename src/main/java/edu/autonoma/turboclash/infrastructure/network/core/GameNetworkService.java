package edu.autonoma.turboclash.infrastructure.network.core;

import edu.autonoma.turboclash.application.GameContext;
import edu.autonoma.turboclash.domain.model.Item;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Obstacle;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.GameStartPayload;
import edu.autonoma.turboclash.infrastructure.network.message.MatchSnapshot;
import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.message.PlayerState;
import edu.autonoma.turboclash.infrastructure.network.message.WorldObjectState;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GameNetworkService {

    private final UdpPeer peer;
    private final GameMessageFactory messageFactory;
    private final NetworkConfig networkConfig;
    private final AtomicLong sequenceGenerator = new AtomicLong(1L);
    private volatile boolean connecting = false;

    public GameNetworkService(UdpPeer peer,
                              GameMessageFactory messageFactory,
                              NetworkConfig networkConfig) {
        this.peer = peer;
        this.messageFactory = messageFactory;
        this.networkConfig = networkConfig;
    }

    public UdpPeer getPeer() {
        return peer;
    }

    public void sendJoin(Player player) {
        if (!canSend(player)) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_JOINED, peer.getLocalPort(),
                nextSequence(), false);
        peer.enviarATodos(msg);
    }

    public void sendHandshake(Player player) {
        if (!canSend(player)) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.HANDSHAKE, peer.getLocalPort(),
                nextSequence(), false);
        peer.enviarATodos(msg);
    }

    public void sendMovement(Player player) {
        if (!canSend(player)) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.MOVEMENT, peer.getLocalPort(),
                nextSequence(), false);
        peer.enviarATodos(msg);
    }

    public void sendLeave(Player player) {
        if (!canSend(player)) {
            return;
        }

        GameMessage msg = messageFactory.create(player, MessageType.PLAYER_LEFT, peer.getLocalPort(),
                nextSequence(), false);
        peer.enviarATodos(msg);
    }

    public void sendGameStart(Player player,
                              int hostPort,
                              long scheduledStartTime,
                              int connectedPlayers,
                              int minPlayers,
                              int maxPlayers) {
        if (!canSend(player)) {
            return;
        }

        long sequence = nextSequence();
        GameStartPayload payload = new GameStartPayload();
        payload.hostPort = hostPort;
        payload.sequence = sequence;
        payload.scheduledStartTime = scheduledStartTime;
        payload.connectedPlayers = connectedPlayers;
        payload.minPlayers = minPlayers;
        payload.maxPlayers = maxPlayers;

        GameMessage message = messageFactory.createEvent(
                player,
                MessageType.GAME_START,
                MessagePayloadCodec.encodeGameStart(payload),
                peer.getLocalPort(),
                sequence,
                true
        );
        message.setAuthority(true);
        peer.enviarATodos(message);
    }

    public void sendSnapshot(Match match, List<Item> items, List<Obstacle> obstacles, int hostPort) {
        if (match == null || match.getLocalPlayer() == null || !peer.isActivo()) {
            return;
        }

        long sequence = nextSequence();
        MatchSnapshot snapshot = buildSnapshot(match, items, obstacles, sequence, hostPort);
        GameMessage message = messageFactory.createEvent(
                match.getLocalPlayer(),
                MessageType.SYNC,
                MessagePayloadCodec.encodeSnapshot(snapshot),
                peer.getLocalPort(),
                sequence,
                true
        );
        message.setAuthority(true);
        peer.enviarATodos(message);
    }

    public void sendGameOver(Match match,
                             List<Item> items,
                             List<Obstacle> obstacles,
                             List<Player> ranking,
                             String reason,
                             int hostPort) {
        if (match == null || match.getLocalPlayer() == null || !peer.isActivo()) {
            return;
        }

        long sequence = nextSequence();
        MatchSnapshot snapshot = buildSnapshot(match, items, obstacles, sequence, hostPort);
        snapshot.finished = true;
        snapshot.started = false;
        snapshot.gameOverReason = reason;
        snapshot.winnerId = match.getWinner() != null ? match.getWinner().getId() : null;
        snapshot.players.clear();

        for (Player player : ranking) {
            snapshot.players.add(toPlayerState(player, player.getLastProcessedSequence()));
        }

        GameMessage message = messageFactory.createEvent(
                match.getLocalPlayer(),
                MessageType.GAME_OVER,
                MessagePayloadCodec.encodeSnapshot(snapshot),
                peer.getLocalPort(),
                sequence,
                true
        );
        message.setAuthority(true);
        peer.enviarATodos(message);
    }

    public void discover() {
        if (!peer.isActivo()) {
            return;
        }

        GameMessage msg = messageFactory.createDiscovery(peer.getLocalPort());

        for (int port : networkConfig.getPorts()) {
            if (port == peer.getLocalPort()) {
                continue;
            }

            try {
                peer.getSender().enviarMensaje(msg, "255.255.255.255", port);
            } catch (Exception ignored) {
            }
        }
    }

    public void join(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo()) {
            return;
        }

        sendJoin(player);
    }

    public void connect(GameContext context, Player player) {
        if (context == null || player == null || !peer.isActivo() || connecting) {
            return;
        }

        connecting = true;

        Thread connectionThread = new Thread(() -> {
            try {
                int maxAttempts = 8;

                for (int i = 0; i < maxAttempts && peer.isActivo(); i++) {
                    sendHandshake(player);
                    sendJoin(player);

                    if (i < 2) {
                        discover();
                    }

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } finally {
                connecting = false;
            }
        });

        connectionThread.setName("Network-Connect-Thread");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    private MatchSnapshot buildSnapshot(Match match,
                                        List<Item> items,
                                        List<Obstacle> obstacles,
                                        long sequence,
                                        int hostPort) {
        MatchSnapshot snapshot = new MatchSnapshot();
        snapshot.hostPort = hostPort;
        snapshot.sequence = sequence;
        snapshot.started = match.isStarted();
        snapshot.finished = match.isFinished();
        snapshot.scheduledStartTime = match.getScheduledStartTime();
        snapshot.remainingMillis = match.getRemainingMillis();
        snapshot.gameOverReason = match.getGameOverReason();
        snapshot.winnerId = match.getWinner() != null ? match.getWinner().getId() : null;

        for (Player player : match.getPlayers()) {
            snapshot.players.add(toPlayerState(player, sequence));
        }

        if (items != null) {
            for (Item item : items) {
                WorldObjectState state = new WorldObjectState();
                state.id = item.getId();
                state.posX = item.getX();
                state.posY = item.getY();
                state.width = item.getWidth();
                state.height = item.getHeight();
                state.visible = item.isVisible();
                snapshot.items.add(state);
            }
        }

        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                WorldObjectState state = new WorldObjectState();
                state.id = obstacle.getId();
                state.posX = obstacle.getX();
                state.posY = obstacle.getY();
                state.width = obstacle.getWidth();
                state.height = obstacle.getHeight();
                state.visible = obstacle.isVisible();
                state.processed = obstacle.isProcessed();
                state.type = obstacle.getType().name();
                snapshot.obstacles.add(state);
            }
        }

        return snapshot;
    }

    private PlayerState toPlayerState(Player player, long sequence) {
        PlayerState state = new PlayerState();
        state.playerId = player.getId();
        state.playerName = player.getName();
        state.port = player.getNetworkPort();
        state.score = player.getCurrentPoints();
        state.lives = player.getLives();
        state.finishReached = player.isFinishReached();
        state.eliminated = player.isEliminated();
        state.finishOrder = player.getFinishOrder();
        state.eliminationOrder = player.getEliminationOrder();
        state.active = player.getCar() != null && player.getCar().isActive();
        state.sequence = sequence;
        if (player.getCar() != null) {
            state.posX = player.getCar().getX();
            state.posY = player.getCar().getY();
        }
        return state;
    }

    private boolean canSend(Player player) {
        return player != null && player.getCar() != null && peer.isActivo();
    }

    private long nextSequence() {
        return sequenceGenerator.getAndIncrement();
    }
}
