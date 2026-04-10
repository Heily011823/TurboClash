package edu.autonoma.turboclash.infrastructure.network.message;

import edu.autonoma.turboclash.infrastructure.network.config.PeerConfigEntry;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Representa la clase `MessagePayloadCodec` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
public final class MessagePayloadCodec {

    private MessagePayloadCodec() {
    }

    /**
     * Ejecuta la operacion publica `encodeGameStart`.
     * @param payload valor del parametro `payload`
     * @return resultado de la operacion documentada
     */
    public static String encodeGameStart(GameStartPayload payload) {
        return payload.hostPort + ";" +
                payload.sequence + ";" +
                payload.scheduledStartTime + ";" +
                payload.connectedPlayers + ";" +
                payload.minPlayers + ";" +
                payload.maxPlayers;
    }

    /**
     * Ejecuta la operacion publica `decodeGameStart`.
     * @param data valor del parametro `data`
     * @return resultado de la operacion documentada
     */
    public static GameStartPayload decodeGameStart(String data) {
        String[] parts = data.split(";", -1);
        GameStartPayload payload = new GameStartPayload();
        payload.hostPort = parseInt(parts, 0);
        payload.sequence = parseLong(parts, 1);
        payload.scheduledStartTime = parseLong(parts, 2);
        payload.connectedPlayers = parseInt(parts, 3);
        payload.minPlayers = parseInt(parts, 4);
        payload.maxPlayers = parseInt(parts, 5);
        return payload;
    }

    /**
     * Ejecuta la operacion publica `encodeSnapshot`.
     * @param snapshot valor del parametro `snapshot`
     * @return resultado de la operacion documentada
     */
    public static String encodeSnapshot(MatchSnapshot snapshot) {
        StringBuilder builder = new StringBuilder();
        builder.append(snapshot.hostPort).append(';')
                .append(snapshot.sequence).append(';')
                .append(snapshot.started).append(';')
                .append(snapshot.finished).append(';')
                .append(snapshot.scheduledStartTime).append(';')
                .append(snapshot.remainingMillis).append(';')
                .append(encode(snapshot.gameOverReason)).append(';')
                .append(encode(snapshot.winnerId));

        for (PlayerState player : snapshot.players) {
            builder.append('\n').append("P;")
                    .append(encode(player.playerId)).append(';')
                    .append(encode(player.playerName)).append(';')
                    .append(player.posX).append(';')
                    .append(player.posY).append(';')
                    .append(player.lives).append(';')
                    .append(player.score).append(';')
                    .append(player.finishReached).append(';')
                    .append(player.eliminated).append(';')
                    .append(player.finishOrder).append(';')
                    .append(player.eliminationOrder).append(';')
                    .append(player.active).append(';')
                    .append(player.port).append(';')
                    .append(player.sequence);
        }

        for (WorldObjectState item : snapshot.items) {
            builder.append('\n').append("I;")
                    .append(encode(item.id)).append(';')
                    .append(item.posX).append(';')
                    .append(item.posY).append(';')
                    .append(item.width).append(';')
                    .append(item.height).append(';')
                    .append(item.visible);
        }

        for (WorldObjectState obstacle : snapshot.obstacles) {
            builder.append('\n').append("O;")
                    .append(encode(obstacle.id)).append(';')
                    .append(obstacle.posX).append(';')
                    .append(obstacle.posY).append(';')
                    .append(obstacle.width).append(';')
                    .append(obstacle.height).append(';')
                    .append(obstacle.visible).append(';')
                    .append(obstacle.processed).append(';')
                    .append(encode(obstacle.type));
        }

        return builder.toString();
    }

    /**
     * Ejecuta la operacion publica `decodeSnapshot`.
     * @param data valor del parametro `data`
     * @return resultado de la operacion documentada
     */
    public static MatchSnapshot decodeSnapshot(String data) {
        String[] lines = data.split("\\R");
        String[] header = lines[0].split(";", -1);
        MatchSnapshot snapshot = new MatchSnapshot();
        snapshot.hostPort = parseInt(header, 0);
        snapshot.sequence = parseLong(header, 1);
        snapshot.started = parseBoolean(header, 2);
        snapshot.finished = parseBoolean(header, 3);
        snapshot.scheduledStartTime = parseLong(header, 4);
        snapshot.remainingMillis = parseLong(header, 5);
        snapshot.gameOverReason = decode(read(header, 6));
        snapshot.winnerId = decode(read(header, 7));

        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }

            String[] parts = lines[i].split(";", -1);
            switch (parts[0]) {
                case "P" -> snapshot.players.add(readPlayer(parts));
                case "I" -> snapshot.items.add(readItem(parts));
                case "O" -> snapshot.obstacles.add(readObstacle(parts));
                default -> {
                }
            }
        }

        return snapshot;
    }

    /**
     * Ejecuta la operacion publica `decodePeers`.
     * @param json valor del parametro `json`
     * @return resultado de la operacion documentada
     */
    public static List<PeerConfigEntry> decodePeers(String json) {
        List<PeerConfigEntry> entries = new ArrayList<>();
        if (json == null) {
            return entries;
        }

        String compact = json.replace("\r", "").replace("\n", "").trim();
        if (compact.length() < 2) {
            return entries;
        }

        String body = compact.substring(1, compact.length() - 1).trim();
        if (body.isBlank()) {
            return entries;
        }

        String[] objects = body.split("\\},\\s*\\{");
        for (String rawObject : objects) {
            String object = rawObject.replace("{", "").replace("}", "").trim();
            PeerConfigEntry entry = new PeerConfigEntry();
            for (String field : object.split(",")) {
                String[] pair = field.split(":", 2);
                if (pair.length < 2) {
                    continue;
                }

                String key = pair[0].replace("\"", "").trim();
                String value = pair[1].replace("\"", "").trim();
                switch (key) {
                    case "nombre" -> entry.setNombre(value);
                    case "ip" -> entry.setIp(value);
                    case "puerto" -> entry.setPuerto(Integer.parseInt(value));
                    default -> {
                    }
                }
            }
            entries.add(entry);
        }

        return entries;
    }

    private static PlayerState readPlayer(String[] parts) {
        PlayerState state = new PlayerState();
        state.playerId = decode(read(parts, 1));
        state.playerName = decode(read(parts, 2));
        state.posX = parseDouble(parts, 3);
        state.posY = parseDouble(parts, 4);
        state.lives = parseInt(parts, 5);
        state.score = parseInt(parts, 6);
        state.finishReached = parseBoolean(parts, 7);
        state.eliminated = parseBoolean(parts, 8);
        state.finishOrder = parseInt(parts, 9);
        state.eliminationOrder = parseInt(parts, 10);
        state.active = parseBoolean(parts, 11);
        state.port = parseInt(parts, 12);
        state.sequence = parseLong(parts, 13);
        return state;
    }

    private static WorldObjectState readItem(String[] parts) {
        WorldObjectState state = new WorldObjectState();
        state.id = decode(read(parts, 1));
        state.posX = parseDouble(parts, 2);
        state.posY = parseDouble(parts, 3);
        state.width = parseInt(parts, 4);
        state.height = parseInt(parts, 5);
        state.visible = parseBoolean(parts, 6);
        return state;
    }

    private static WorldObjectState readObstacle(String[] parts) {
        WorldObjectState state = readItem(parts);
        state.processed = parseBoolean(parts, 7);
        state.type = decode(read(parts, 8));
        return state;
    }

    private static String encode(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }

    private static String read(String[] parts, int index) {
        return index < parts.length ? parts[index] : "";
    }

    private static int parseInt(String[] parts, int index) {
        String value = read(parts, index);
        return value.isBlank() ? 0 : Integer.parseInt(value);
    }

    private static long parseLong(String[] parts, int index) {
        String value = read(parts, index);
        return value.isBlank() ? 0L : Long.parseLong(value);
    }

    private static double parseDouble(String[] parts, int index) {
        String value = read(parts, index);
        return value.isBlank() ? 0.0 : Double.parseDouble(value);
    }

    private static boolean parseBoolean(String[] parts, int index) {
        return Boolean.parseBoolean(read(parts, index));
    }
}
