package edu.autonoma.turboclash.infrastructure.network.message;

import java.util.ArrayList;
import java.util.List;

public class MatchSnapshot {
    public int hostPort;
    public long sequence;
    public boolean started;
    public boolean finished;
    public long scheduledStartTime;
    public long remainingMillis;
    public String gameOverReason;
    public String winnerId;
    public List<PlayerState> players = new ArrayList<>();
    public List<WorldObjectState> items = new ArrayList<>();
    public List<WorldObjectState> obstacles = new ArrayList<>();
}
