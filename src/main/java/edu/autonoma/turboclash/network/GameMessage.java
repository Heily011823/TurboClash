package edu.autonoma.turboclash.network;

public class GameMessage {

    public MessageType type;
    public String playerId;
    public String playerName;
    public double posX;
    public double posY;
    public int score;
    public long time;
    public String event;


    public String serialize() {
        return type + "|" +
                safe(playerId) + "|" +
                safe(playerName) + "|" +
                posX + "|" +
                posY + "|" +
                score + "|" +
                time + "|" +
                safe(event);
    }

    // Convierte el String recibido en objeto
    public static GameMessage deserialize(String data) {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacío o nulo");
        }

        String[] parts = data.split("\\|");

        // Validación de estructura
        if (parts.length < 8) {
            throw new IllegalArgumentException("Mensaje UDP inválido: " + data);
        }

        GameMessage msg = new GameMessage();

        try {
            msg.type = MessageType.valueOf(parts[0]);
            msg.playerId = parts[1];
            msg.playerName = parts[2];
            msg.posX = Double.parseDouble(parts[3]);
            msg.posY = Double.parseDouble(parts[4]);
            msg.score = Integer.parseInt(parts[5]);
            msg.time = Long.parseLong(parts[6]);
            msg.event = parts[7];

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear mensaje UDP: " + data, e);
        }

        return msg;
    }


    private String safe(String value) {
        if (value == null) return "";
        return value.replace("|", "/");
    }
}