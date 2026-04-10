package edu.autonoma.turboclash.infrastructure.network.message;

import edu.autonoma.turboclash.domain.model.CarSkin;

/**
 * Representa la clase `GameMessage` y define su responsabilidad dentro del sistema.
 *
 * @author 
 * @version 1.0
 */
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

    /**
     * Crea una nueva instancia de `GameMessage`.
     */
    public GameMessage() {
    }

    /**
     * Crea una nueva instancia de `GameMessage`.
     * @param type valor del parametro `type`
     * @param playerId valor del parametro `playerId`
     * @param playerName valor del parametro `playerName`
     * @param posX valor del parametro `posX`
     * @param posY valor del parametro `posY`
     * @param score valor del parametro `score`
     * @param lives valor del parametro `lives`
     * @param time valor del parametro `time`
     * @param sequence valor del parametro `sequence`
     * @param event valor del parametro `event`
     * @param carSkin valor del parametro `carSkin`
     * @param port valor del parametro `port`
     * @param finishReached valor del parametro `finishReached`
     * @param eliminated valor del parametro `eliminated`
     * @param finishOrder valor del parametro `finishOrder`
     * @param eliminationOrder valor del parametro `eliminationOrder`
     * @param active valor del parametro `active`
     * @param authority valor del parametro `authority`
     */
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

    /**
     * Obtiene el valor asociado a `getType`.
     * @return resultado de la operacion documentada
     */
    public MessageType getType() { return type; }
    /**
     * Actualiza el valor asociado a `setType`.
     * @param type valor del parametro `type`
     */
    public void setType(MessageType type) { this.type = type; }

    /**
     * Obtiene el valor asociado a `getPlayerId`.
     * @return resultado de la operacion documentada
     */
    public String getPlayerId() { return playerId; }
    /**
     * Actualiza el valor asociado a `setPlayerId`.
     * @param playerId valor del parametro `playerId`
     */
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    /**
     * Obtiene el valor asociado a `getPlayerName`.
     * @return resultado de la operacion documentada
     */
    public String getPlayerName() { return playerName; }
    /**
     * Actualiza el valor asociado a `setPlayerName`.
     * @param playerName valor del parametro `playerName`
     */
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    /**
     * Obtiene el valor asociado a `getPosX`.
     * @return resultado de la operacion documentada
     */
    public double getPosX() { return posX; }
    /**
     * Actualiza el valor asociado a `setPosX`.
     * @param posX valor del parametro `posX`
     */
    public void setPosX(double posX) { this.posX = posX; }

    /**
     * Obtiene el valor asociado a `getPosY`.
     * @return resultado de la operacion documentada
     */
    public double getPosY() { return posY; }
    /**
     * Actualiza el valor asociado a `setPosY`.
     * @param posY valor del parametro `posY`
     */
    public void setPosY(double posY) { this.posY = posY; }

    /**
     * Obtiene el valor asociado a `getScore`.
     * @return resultado de la operacion documentada
     */
    public int getScore() { return score; }
    /**
     * Actualiza el valor asociado a `setScore`.
     * @param score valor del parametro `score`
     */
    public void setScore(int score) { this.score = score; }

    /**
     * Obtiene el valor asociado a `getLives`.
     * @return resultado de la operacion documentada
     */
    public int getLives() { return lives; }
    /**
     * Actualiza el valor asociado a `setLives`.
     * @param lives valor del parametro `lives`
     */
    public void setLives(int lives) { this.lives = lives; }

    /**
     * Obtiene el valor asociado a `getTime`.
     * @return resultado de la operacion documentada
     */
    public long getTime() { return time; }
    /**
     * Actualiza el valor asociado a `setTime`.
     * @param time valor del parametro `time`
     */
    public void setTime(long time) { this.time = time; }

    /**
     * Obtiene el valor asociado a `getSequence`.
     * @return resultado de la operacion documentada
     */
    public long getSequence() { return sequence; }
    /**
     * Actualiza el valor asociado a `setSequence`.
     * @param sequence valor del parametro `sequence`
     */
    public void setSequence(long sequence) { this.sequence = sequence; }

    /**
     * Obtiene el valor asociado a `getEvent`.
     * @return resultado de la operacion documentada
     */
    public String getEvent() { return event; }
    /**
     * Actualiza el valor asociado a `setEvent`.
     * @param event valor del parametro `event`
     */
    public void setEvent(String event) { this.event = event; }

    /**
     * Obtiene el valor asociado a `getCarSkin`.
     * @return resultado de la operacion documentada
     */
    public CarSkin getCarSkin() { return carSkin; }
    /**
     * Actualiza el valor asociado a `setCarSkin`.
     * @param carSkin valor del parametro `carSkin`
     */
    public void setCarSkin(CarSkin carSkin) { this.carSkin = carSkin; }

    /**
     * Obtiene el valor asociado a `getPort`.
     * @return resultado de la operacion documentada
     */
    public int getPort() { return port; }
    /**
     * Actualiza el valor asociado a `setPort`.
     * @param port valor del parametro `port`
     */
    public void setPort(int port) { this.port = port; }

    /**
     * Indica la condicion evaluada por `isFinishReached`.
     * @return resultado de la operacion documentada
     */
    public boolean isFinishReached() { return finishReached; }
    /**
     * Actualiza el valor asociado a `setFinishReached`.
     * @param finishReached valor del parametro `finishReached`
     */
    public void setFinishReached(boolean finishReached) { this.finishReached = finishReached; }

    /**
     * Indica la condicion evaluada por `isEliminated`.
     * @return resultado de la operacion documentada
     */
    public boolean isEliminated() { return eliminated; }
    /**
     * Actualiza el valor asociado a `setEliminated`.
     * @param eliminated valor del parametro `eliminated`
     */
    public void setEliminated(boolean eliminated) { this.eliminated = eliminated; }

    /**
     * Obtiene el valor asociado a `getFinishOrder`.
     * @return resultado de la operacion documentada
     */
    public int getFinishOrder() { return finishOrder; }
    /**
     * Actualiza el valor asociado a `setFinishOrder`.
     * @param finishOrder valor del parametro `finishOrder`
     */
    public void setFinishOrder(int finishOrder) { this.finishOrder = finishOrder; }

    /**
     * Obtiene el valor asociado a `getEliminationOrder`.
     * @return resultado de la operacion documentada
     */
    public int getEliminationOrder() { return eliminationOrder; }
    /**
     * Actualiza el valor asociado a `setEliminationOrder`.
     * @param eliminationOrder valor del parametro `eliminationOrder`
     */
    public void setEliminationOrder(int eliminationOrder) { this.eliminationOrder = eliminationOrder; }

    /**
     * Indica la condicion evaluada por `isActive`.
     * @return resultado de la operacion documentada
     */
    public boolean isActive() { return active; }
    /**
     * Actualiza el valor asociado a `setActive`.
     * @param active valor del parametro `active`
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Indica la condicion evaluada por `isAuthority`.
     * @return resultado de la operacion documentada
     */
    public boolean isAuthority() { return authority; }
    /**
     * Actualiza el valor asociado a `setAuthority`.
     * @param authority valor del parametro `authority`
     */
    public void setAuthority(boolean authority) { this.authority = authority; }

    /**
     * Ejecuta la operacion publica `serialize`.
     * @return resultado de la operacion documentada
     */
    public String serialize() {
        String serialized = getType() + "|" +
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
        System.out.println("[DEBUG][GameMessage] Serializando mensaje type=" + getType()
                + " playerId=" + playerId
                + " sequence=" + sequence
                + " authority=" + authority);
        return serialized;
    }

    /**
     * Ejecuta la operacion publica `deserialize`.
     * @param data valor del parametro `data`
     * @return resultado de la operacion documentada
     */
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

        System.out.println("[DEBUG][GameMessage] Deserializando mensaje type=" + msg.getType()
                + " playerId=" + msg.getPlayerId()
                + " sequence=" + msg.getSequence()
                + " authority=" + msg.isAuthority());
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
