package edu.autonoma.turboclash.infrastructure.network.message;

public class PlayerState {
    public String playerId;
    public String playerName;
    public double posX;
    public double posY;
    public int lives;
    public int score;
    public boolean finishReached;
    public boolean eliminated;
    public int finishOrder;
    public int eliminationOrder;
    public boolean active;
    public int port;
    public long sequence;
}
