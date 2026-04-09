package edu.autonoma.turboclash.infrastructure.network.handler;

import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.strategy.IMessageStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.JoinStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.LeaveStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.MoveStrategy;
import edu.autonoma.turboclash.infrastructure.network.strategy.ScoreStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Procesa mensajes de red del juego.
 */
public class GameMessageHandler {

    private final Map<MessageType, IMessageStrategy> strategies = new HashMap<>();
    private final Match match;
    private UdpPeer peer;
    private GameMessageFactory messageFactory;

    private final Set<String> processedJoins = ConcurrentHashMap.newKeySet();

    public GameMessageHandler(Match match) {
        this.match = match;

        strategies.put(MessageType.HANDSHAKE, new JoinStrategy(match));
        strategies.put(MessageType.PLAYER_JOINED, new JoinStrategy(match));
        strategies.put(MessageType.MOVEMENT, new MoveStrategy(match));
        strategies.put(MessageType.SCORE_UPDATE, new ScoreStrategy(match));
        strategies.put(MessageType.PLAYER_LEFT, new LeaveStrategy(match));
    }

    public void handle(GameMessage msg, String ip, int port) {
        if (msg == null || match == null) {
            return;
        }

        Player localPlayer = match.getLocalPlayer();

        if (peer != null && msg.getPort() == peer.getLocalPort()) {
            return;
        }

        if (localPlayer != null) {
            boolean sameAsLocalById =
                    localPlayer.getId() != null
                            && msg.getPlayerId() != null
                            && localPlayer.getId().equals(msg.getPlayerId());

            boolean sameAsLocalByName =
                    localPlayer.getName() != null
                            && msg.getPlayerName() != null
                            && localPlayer.getName().equalsIgnoreCase(msg.getPlayerName());

            if (sameAsLocalById || sameAsLocalByName) {
                return;
            }
        }

        if (msg.getType() == MessageType.DISCOVERY) {
            if (peer != null) {
                peer.agregarPeer(ip, port);
            }

            if (peer != null && messageFactory != null && localPlayer != null) {
                GameMessage handshake = messageFactory.create(localPlayer, MessageType.HANDSHAKE, peer.getLocalPort());
                peer.getSender().enviarMensaje(handshake, ip, port);

                GameMessage join = messageFactory.create(localPlayer, MessageType.PLAYER_JOINED, peer.getLocalPort());
                peer.getSender().enviarMensaje(join, ip, port);
            }
            return;
        }

        if (msg.getType() == MessageType.HANDSHAKE || msg.getType() == MessageType.PLAYER_JOINED) {
            String joinKey = buildJoinKey(msg);

            if (!processedJoins.add(joinKey)) {
                return;
            }

            if (peer != null) {
                peer.agregarPeer(ip, port);
            }

            if (msg.getType() == MessageType.HANDSHAKE
                    && peer != null
                    && messageFactory != null
                    && localPlayer != null) {
                GameMessage joinResponse = messageFactory.create(localPlayer, MessageType.PLAYER_JOINED, peer.getLocalPort());
                peer.getSender().enviarMensaje(joinResponse, ip, port);
            }
        }

        IMessageStrategy strategy = strategies.get(msg.getType());
        if (strategy != null) {
            strategy.handle(msg);
        }

        System.out.println("Procesado: " + msg.getType()
                + " de " + msg.getPlayerName()
                + " puerto=" + msg.getPort()
                + " | remotos: " + match.getRemotePlayers().size());
    }

    private String buildJoinKey(GameMessage msg) {
        String playerId = msg.getPlayerId() != null ? msg.getPlayerId() : "";
        String playerName = msg.getPlayerName() != null ? msg.getPlayerName() : "";
        return msg.getType() + "|" + playerId + "|" + playerName + "|" + msg.getPort();
    }

    public void setPeer(UdpPeer peer) {
        this.peer = peer;
    }

    public void setMessageFactory(GameMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }
}