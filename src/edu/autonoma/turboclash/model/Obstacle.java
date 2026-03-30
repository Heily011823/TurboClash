package edu.autonoma.turboclash.model;

/**
 * Represents a hazardous object in the game world that penalizes the player.
 * * Design Principles:
 * - Inheritance: Extends GameObject to reuse physics and collision logic.
 * - Single Responsibility (SRP): Manages its own penalty values and affects the player state.
 * - Encapsulation: State is protected and behavior is exposed through clear methods.
 */
public class Obstacle extends GameObject {
    private final int penalty;
    private final String type;

    /**
     * Constructs a new Obstacle with specific penalty and attributes.
     * * @param id       Unique identifier for the obstacle instance.
     * @param posX     Initial X coordinate.
     * @param posY     Initial Y coordinate.
     * @param width    Width of the collision box.
     * @param height   Height of the collision box.
     * @param penalty  Points to be deducted (refactored to ensure it's handled as a magnitude).
     * @param type     The category of obstacle (e.g., "OIL_SPILL", "WALL", "CONE").
     */
    public Obstacle(String id, double posX, double posY, int width, int height, int penalty, String type) {
        super(id, posX, posY, width, height);
        // Refactoring: Ensure penalty is stored as a positive value to avoid logic confusion
        this.penalty = Math.abs(penalty);
        this.type = type;
    }

    /**
     * Applies the obstacle's penalty to the player.
     * * @param player The player instance to be affected.
     */
    public void affectPlayer(Player player) {
        if (player != null && this.visible) {
            player.subtractPoints(this.penalty);
            // Optional: Depending on game rules, an obstacle might disappear or stay visible
        }
    }

    /**
     * Renders the obstacle on the screen.
     */
    @Override
    public void draw() {
        if (this.visible) {
            // Graphical implementation for the specific obstacle type
        }
    }



    public int getPenalty() {
        return penalty;
    }

    public String getType() {
        return type;
    }
}