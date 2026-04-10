package edu.autonoma.turboclash.infrastructure.network.message;

public class GameStartPayload {
    public int hostPort;
    public long sequence;
    public long scheduledStartTime;
    public int connectedPlayers;
    public int minPlayers;
    public int maxPlayers;
}
