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
        return type + "|" + playerId + "|" + playerName + "|" +
                posX + "|" + posY + "|" + score + "|" + time + "|" + event;
    }

    public static GameMessage deserialize(String data) {
        String[] parts = data.split("\\|");

        GameMessage msg = new GameMessage();
        msg.type = MessageType.valueOf(parts[0]);
        msg.playerId = parts[1];
        msg.playerName = parts[2];
        msg.posX = Double.parseDouble(parts[3]);
        msg.posY = Double.parseDouble(parts[4]);
        msg.score = Integer.parseInt(parts[5]);
        msg.time = Long.parseLong(parts[6]);
        msg.event = parts[7];

        return msg;
    }
}
