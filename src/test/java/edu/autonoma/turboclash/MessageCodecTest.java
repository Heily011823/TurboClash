package edu.autonoma.turboclash;

import edu.autonoma.turboclash.infrastructure.network.message.GameMessage;
import edu.autonoma.turboclash.infrastructure.network.message.GameStartPayload;
import edu.autonoma.turboclash.infrastructure.network.message.MatchSnapshot;
import edu.autonoma.turboclash.infrastructure.network.message.MessagePayloadCodec;
import edu.autonoma.turboclash.infrastructure.network.message.MessageType;
import edu.autonoma.turboclash.infrastructure.network.message.PlayerState;

/**
 * Representa la clase `MessageCodecTest` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public final class MessageCodecTest {

    private MessageCodecTest() {
    }

    /**
     * Ejecuta la operacion publica `run`.
     */
    public static void run() {
        GameMessage message = new GameMessage(
                MessageType.MOVEMENT,
                "5001",
                "Host",
                120.5,
                240.0,
                15,
                0,
                1000L,
                25L,
                "",
                null,
                5001,
                true,
                true,
                1,
                2,
                false,
                true
        );

        GameMessage parsed = GameMessage.deserialize(message.serialize());
        GameRulesManagerTest.assertEquals(0, parsed.getLives(), "serialized lives should preserve zero");
        GameRulesManagerTest.assertTrue(parsed.isEliminated(), "serialized eliminated flag should be true");

        GameStartPayload startPayload = new GameStartPayload();
        startPayload.hostPort = 5001;
        startPayload.sequence = 3L;
        startPayload.scheduledStartTime = 9999L;
        startPayload.connectedPlayers = 3;
        startPayload.minPlayers = 2;
        startPayload.maxPlayers = 4;
        GameStartPayload decodedStart = MessagePayloadCodec.decodeGameStart(MessagePayloadCodec.encodeGameStart(startPayload));
        GameRulesManagerTest.assertEquals(5001, decodedStart.hostPort, "game start host should roundtrip");

        MatchSnapshot snapshot = new MatchSnapshot();
        snapshot.hostPort = 5001;
        snapshot.sequence = 10L;
        snapshot.started = true;
        snapshot.remainingMillis = 5000L;
        PlayerState state = new PlayerState();
        state.playerId = "5002";
        state.playerName = "Remote";
        state.lives = 0;
        state.score = 12;
        state.eliminated = true;
        state.active = false;
        snapshot.players.add(state);

        MatchSnapshot decodedSnapshot = MessagePayloadCodec.decodeSnapshot(MessagePayloadCodec.encodeSnapshot(snapshot));
        GameRulesManagerTest.assertEquals(1, decodedSnapshot.players.size(), "snapshot should preserve player count");
        GameRulesManagerTest.assertEquals(0, decodedSnapshot.players.get(0).lives, "snapshot should preserve zero lives");
    }
}
