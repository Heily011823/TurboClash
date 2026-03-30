package edu.autonoma.turboclash.model;

/**
 * Represents a player in the TurboClash game.
 *
 * Design Principles Applied:
 * - SRP: Delegates car physics and score math to their respective objects.
 * - Defensive Programming: Validates player state (connection) before actions.
 * - DIP (Dependency Inversion): Score could be injected (conceptually).
 */
public class Player {
    private final String id;
    private final String name;
    private final String ip;
    private boolean connected;
    private final Car car;
    private final Score score;

    /**
     * Constructs a new Player.
     * Note: Score is initialized internally to ensure a fresh state for new players.
     */
    public Player(String id, String name, String ip, Car car) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.car = car;
        this.score = new Score();
        this.connected = true;
    }

    /**
     * Moves the car only if the player is currently connected.
     */
    public void moveCar(double x, double y) {
        if (connected && car != null) {
            car.moveTo(x, y);
        }
    }

    /**
     * Adds points ensuring the player is active in the session.
     */
    public void addPoints(int points) {
        if (connected) {
            score.update(points);
        }
    }

    /**
     * Subtracts points ensuring the player is active in the session.
     */
    public void subtractPoints(int points) {
        if (connected) {
            score.update(-points);
        }
    }

    // --- Getters & Setters ---

    public boolean isConnected() { return connected; }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getIp() { return ip; }
    public Car getCar() { return car; }

    /**
     * Returns the current point value instead of the Score object
     * to protect internal state encapsulation.
     */
    public int getCurrentPoints() {
        return score.getPoints();
    }


    public Score getScore() { return score; }
}