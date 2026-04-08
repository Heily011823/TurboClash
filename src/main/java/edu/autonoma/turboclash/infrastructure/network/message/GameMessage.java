package edu.autonoma.turboclash.infrastructure.network.message;

import edu.autonoma.turboclash.domain.model.CarSkin;

/**
 * Representa la responsabilidad de {@code GameMessage} en el intercambio de mensajes.
 */
public class GameMessage {

    private MessageType type;
    private String playerId;
    private String playerName;
    private double posX;
    private double posY;
    private int score;
    private long time;
    private String event;


    private CarSkin carSkin;

    public GameMessage() {}

    /**
     * Crea una nueva instancia de {@code GameMessage}.
     *
     * @param type valor del parametro {@code type}
     * @param playerId valor del parametro {@code playerId}
     * @param playerName valor del parametro {@code playerName}
     * @param posX valor del parametro {@code posX}
     * @param posY valor del parametro {@code posY}
     * @param score valor del parametro {@code score}
     * @param time valor del parametro {@code time}
     * @param event valor del parametro {@code event}
     * @param carSkin valor del parametro {@code carSkin}
     */
    public GameMessage(MessageType type, String playerId, String playerName,
                       double posX, double posY, int score, long time, String event, CarSkin carSkin) {
        this.type = type;
        this.playerId = playerId;
        this.playerName = playerName;
        this.posX = posX;
        this.posY = posY;
        this.score = score;
        this.time = time;
        this.event = event;
        this.carSkin = carSkin;
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

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }


    public CarSkin getCarSkin() { return carSkin; }
    public void setCarSkin(CarSkin carSkin) { this.carSkin = carSkin; }


    /**
     * Ejecuta la operacion {@code serialize}.
     *
     * @return resultado de la operacion {@code serialize}
     */
    public String serialize() {
        return getType() + "|" +
                safe(playerId) + "|" +
                safe(playerName) + "|" +
                posX + "|" +
                posY + "|" +
                score + "|" +
                time + "|" +
                safe(event) + "|" +
                safe(carSkin != null ? carSkin.name() : "");
    }


    /**
     * Ejecuta la operacion {@code deserialize}.
     *
     * @param data valor del parametro {@code data}
     * @return resultado de la operacion {@code deserialize}
     */
    public static GameMessage deserialize(String data) {

        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Mensaje vacío o nulo");
        }

        String[] parts = data.split("\\|", -1);

        if (parts.length < 9) {
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


            if (!parts[8].isEmpty()) {
                msg.setCarSkin(CarSkin.valueOf(parts[8]));
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear mensaje UDP: " + data, e);
        }

        return msg;
    }

    /**
     * Ejecuta la operacion {@code safe}.
     *
     * @param value valor del parametro {@code value}
     * @return resultado de la operacion {@code safe}
     */
    private String safe(String value) {
        if (value == null) return "";
        return value.replace("|", "/");
    }
}
