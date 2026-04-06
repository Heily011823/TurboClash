package edu.autonoma.turboclash.network.message;

public class GameMessage {

    private MessageType type;
    private String playerId;
    private String playerName;
    private double posX;
    private double posY;
    private int score;
    private long time;
    private String event;

    public GameMessage() {}

    public GameMessage(MessageType type, String playerId, String playerName,
                       double posX, double posY, int score, long time, String event) {
        this.type = type;
        this.playerId = playerId;
        this.playerName = playerName;
        this.posX = posX;
        this.posY = posY;
        this.score = score;
        this.time = time;
        this.event = event;
    }

    // GETTERS Y SETTERS
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public double getPosX() { return posX; }
    public void setPosX(double posX) { this.posX = posX; }

    public double getPosY() { return posY; }
    public void setPosY(double posY) { this.posY = posY; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }


    public String serialize() {
        return getType() + "|" +
                safe(playerId) + "|" +
                safe(playerName) + "|" +
                posX + "|" +
                posY + "|" +
                score + "|" +
                time + "|" +
                safe(event);
    }


    public static GameMessage deserialize(String data) {

        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacío o nulo");
        }

        String[] parts = data.split("\\|");

        if (parts.length < 8) {
            throw new IllegalArgumentException("Mensaje UDP inválido: " + data);
        }

        GameMessage msg = new GameMessage();

        try {
            msg.setType(MessageType.valueOf(parts[0].trim()));
            msg.setPlayerId(parts[1]);
            msg.setPlayerName(parts[2]);
            msg.setPosX(Double.parseDouble(parts[3]));
            msg.setPosY(Double.parseDouble(parts[4]));
            msg.setScore(Integer.parseInt(parts[5]));
            msg.setTime(Long.parseLong(parts[6]));
            msg.setEvent(parts[7]);

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