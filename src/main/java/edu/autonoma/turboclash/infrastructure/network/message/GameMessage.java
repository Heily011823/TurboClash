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
    private long sequence;
    private String event;
    private CarSkin carSkin;
    private int port;
    private boolean finishReached;
    private boolean eliminated;
    private int finishOrder;
    private int eliminationOrder;
    private boolean active;
    private boolean authority;

    public GameMessage() {
    }

    public GameMessage(MessageType type,
                       String playerId,
                       String playerName,
                       double posX,
                       double posY,
                       int score,
                       int lives,
                       long time,
                       long sequence,
                       String event,
                       CarSkin carSkin,
                       int port,
                       boolean finishReached,
                       boolean eliminated,
                       int finishOrder,
                       int eliminationOrder,
                       boolean active,
                       boolean authority) {

        this.type = type;
        this.playerId = playerId != null ? playerId : "unknown";
        this.playerName = playerName != null ? playerName : "unknown";
        this.posX = posX;
        this.posY = posY;
        this.score = score;
        this.lives = lives;
        this.time = time;
        this.sequence = sequence;
        this.event = event;
        this.carSkin = carSkin;
        this.port = port;
        this.finishReached = finishReached;
        this.eliminated = eliminated;
        this.finishOrder = finishOrder;
        this.eliminationOrder = eliminationOrder;
        this.active = active;
        this.authority = authority;
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

    public long getSequence() { return sequence; }
    public void setSequence(long sequence) { this.sequence = sequence; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public CarSkin getCarSkin() { return carSkin; }
    public void setCarSkin(CarSkin carSkin) { this.carSkin = carSkin; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public boolean isFinishReached() { return finishReached; }
    public void setFinishReached(boolean finishReached) { this.finishReached = finishReached; }

    public boolean isEliminated() { return eliminated; }
    public void setEliminated(boolean eliminated) { this.eliminated = eliminated; }

    public int getFinishOrder() { return finishOrder; }
    public void setFinishOrder(int finishOrder) { this.finishOrder = finishOrder; }

    public int getEliminationOrder() { return eliminationOrder; }
    public void setEliminationOrder(int eliminationOrder) { this.eliminationOrder = eliminationOrder; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isAuthority() { return authority; }
    public void setAuthority(boolean authority) { this.authority = authority; }

    public String serialize() {
        return getType() + "|" +
                safe(playerId) + "|" +
                safe(playerName) + "|" +
                posX + "|" +
                posY + "|" +
                score + "|" +
                lives + "|" +
                time + "|" +
                sequence + "|" +
                safe(event) + "|" +
                safe(carSkin != null ? carSkin.name() : "") + "|" +
                port + "|" +
                finishReached + "|" +
                eliminated + "|" +
                finishOrder + "|" +
                eliminationOrder + "|" +
                active + "|" +
                authority;
    }

    public static GameMessage deserialize(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacio o nulo");
        }

        String[] parts = data.split("\\|", -1);

        if (parts.length < 18) {
            throw new IllegalArgumentException("Mensaje UDP invalido: " + data);
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
            msg.setSequence(Long.parseLong(parts[8]));
            msg.setEvent(emptyToNull(parts[9]));

            if (!parts[10].isBlank()) {
                msg.setCarSkin(CarSkin.valueOf(parts[10].trim()));
            }

            msg.setPort(Integer.parseInt(parts[11].trim()));
            msg.setFinishReached(Boolean.parseBoolean(parts[12].trim()));
            msg.setEliminated(Boolean.parseBoolean(parts[13].trim()));
            msg.setFinishOrder(Integer.parseInt(parts[14].trim()));
            msg.setEliminationOrder(Integer.parseInt(parts[15].trim()));
            msg.setActive(Boolean.parseBoolean(parts[16].trim()));
            msg.setAuthority(Boolean.parseBoolean(parts[17].trim()));

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear mensaje UDP: " + data, e);
        }

        return msg;
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/");
    }

    private static String emptyToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
