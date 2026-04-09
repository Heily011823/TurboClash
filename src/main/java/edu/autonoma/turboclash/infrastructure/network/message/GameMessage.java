package edu.autonoma.turboclash.infrastructure.network.message;

import edu.autonoma.turboclash.domain.model.CarSkin;

public class GameMessage {

    private MessageType type;
    private String playerId;
    private String playerName;
    private double posX;
    private double posY;
    private int score;
    private int lives;
    private long time;
    private String event;
    private CarSkin carSkin;
    private int port;

    public GameMessage() {}

    public GameMessage(MessageType type, String playerId, String playerName,
                       double posX, double posY, int score, int lives, long time,
                       String event, CarSkin carSkin, int port) {

        this.type = type;
        this.playerId = playerId != null ? playerId : "unknown";
        this.playerName = playerName != null ? playerName : "unknown";
        this.posX = posX;
        this.posY = posY;
        this.score = score;
        this.lives = lives;
        this.time = time;
        this.event = event;
        this.carSkin = carSkin;
        this.port = port;
    }

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

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public CarSkin getCarSkin() { return carSkin; }
    public void setCarSkin(CarSkin carSkin) { this.carSkin = carSkin; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String serialize() {
        return getType() + "|" +
                safe(playerId) + "|" +
                safe(playerName) + "|" +
                posX + "|" +
                posY + "|" +
                score + "|" +
                lives + "|" +
                time + "|" +
                safe(event) + "|" +
                safe(carSkin != null ? carSkin.name() : "") + "|" +
                port;
    }

    public static GameMessage deserialize(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacío o nulo");
        }

        String[] parts = data.split("\\|", -1);

        if (parts.length < 11) {
            throw new IllegalArgumentException("Mensaje UDP inválido: " + data);
        }

        GameMessage msg = new GameMessage();

        try {
            msg.setType(MessageType.valueOf(parts[0].trim()));
            msg.setPlayerId(emptyToNull(parts[1]));
            msg.setPlayerName(emptyToNull(parts[2]));
            msg.setPosX(Double.parseDouble(parts[3]));
            msg.setPosY(Double.parseDouble(parts[4]));
            msg.setScore(Integer.parseInt(parts[5]));
            msg.setLives(Integer.parseInt(parts[6]));
            msg.setTime(Long.parseLong(parts[7]));
            msg.setEvent(emptyToNull(parts[8]));

            if (!parts[9].isBlank()) {
                msg.setCarSkin(CarSkin.valueOf(parts[9].trim()));
            }

            msg.setPort(Integer.parseInt(parts[10].trim()));

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear mensaje UDP: " + data, e);
        }

        return msg;
    }

    private String safe(String value) {
        if (value == null) return "";
        return value.replace("|", "/");
    }

    private static String emptyToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}