package edu.autonoma.turboclash.infrastructure.network.handler;

import edu.autonoma.turboclash.infrastructure.network.strategy.*;
import edu.autonoma.turboclash.domain.model.Match;
import edu.autonoma.turboclash.domain.model.Player;
import edu.autonoma.turboclash.infrastructure.network.core.UdpPeer;
import edu.autonoma.turboclash.infrastructure.network.factory.GameMessageFactory;
import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa la responsabilidad de {@code GameMessageHandler} en el procesamiento de mensajes de red.
 */
public class GameMessageHandler {

    private final Map<MessageType, IMessageStrategy> strategies = new HashMap<>();
    private final Match match;
    private UdpPeer peer;
    private GameMessageFactory messageFactory;

    /**
     * Crea una nueva instancia de {@code GameMessageHandler}.
     *
     * @param match valor del parametro {@code match}
     */
    public GameMessageHandler(Match match) {
        this.match = match;

        strategies.put(MessageType.HANDSHAKE, new JoinStrategy(match));
        strategies.put(MessageType.PLAYER_JOINED, new JoinStrategy(match));
        strategies.put(MessageType.MOVEMENT, new MoveStrategy(match));
        strategies.put(MessageType.SCORE_UPDATE, new ScoreStrategy(match));
        strategies.put(MessageType.PLAYER_LEFT, new LeaveStrategy(match));
    }

    /**
     * Procesa la operacion principal del metodo.
     *
     * @param msg valor del parametro {@code msg}
     * @param ip direccion IP asociada a la operacion
     * @param port valor del parametro {@code port}
     */
    public void handle(GameMessage msg, String ip, int port) {
        if (msg == null) return;

        if (msg.getType() == MessageType.DISCOVERY) {
            if (peer != null) {
                peer.agregarPeer(ip, port);
            }

            if (peer != null && messageFactory != null && match.getLocalPlayer() != null) {
                GameMessage response = messageFactory.create(
                        match.getLocalPlayer(),
                        MessageType.HANDSHAKE
                );
                peer.getSender().enviarMensaje(response, ip, port);
            }
            return;
        }

        if (msg.getType() == MessageType.HANDSHAKE) {
            if (peer != null) {
                peer.agregarPeer(ip, port);
            }
        }

        if (msg.getPlayerId() != null
                && msg.getPlayerId().equals(match.getLocalPlayer().getId())) {
            return;
        }

        IMessageStrategy strategy = strategies.get(msg.getType());

        if (strategy != null) {
            strategy.handle(msg);

            if (msg.getType() == MessageType.PLAYER_JOINED
                    && peer != null
                    && messageFactory != null
                    && match.getLocalPlayer() != null) {
                GameMessage localPlayerMessage = messageFactory.create(
                        match.getLocalPlayer(),
                        MessageType.HANDSHAKE
                );
                peer.getSender().enviarMensaje(localPlayerMessage, ip, port);

                for (Player remotePlayer : match.getRemotePlayers()) {
                    if (remotePlayer.getId().equals(msg.getPlayerId())) {
                        continue;
                    }

                    GameMessage knownRemoteMessage = messageFactory.create(
                            remotePlayer,
                            MessageType.PLAYER_JOINED
                    );
                    peer.getSender().enviarMensaje(knownRemoteMessage, ip, port);
                }

                peer.enviarATodosExcepto(msg, ip, port);
            }

            if ((msg.getType() == MessageType.MOVEMENT
                    || msg.getType() == MessageType.PLAYER_LEFT
                    || msg.getType() == MessageType.SCORE_UPDATE)
                    && peer != null
                    && peer.getPeerCount() > 1) {
                peer.enviarATodosExcepto(msg, ip, port);
            }
        }
    }

    public void setPeer(UdpPeer peer) {
        this.peer = peer;
    }

    public void setMessageFactory(GameMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }
}
