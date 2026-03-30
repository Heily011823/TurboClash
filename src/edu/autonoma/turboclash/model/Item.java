package edu.autonoma.turboclash.model;

/**
 * Represents a collectible object that provides benefits to the player.
 * * Design Principles:
 * - Single Responsibility (SRP): Manages its own state (value/type) and triggers effects.
 * - Liskov Substitution (LSP): Can be used wherever a GameObject is expected.
 * - Open/Closed: New item types can be added by varying the 'type' and 'value'
 * without changing the base logic.
 */
public class Item extends GameObject {
    private final int value;
    private final String type;

    /**
     * Constructs a new collectible Item.
     * * @param id     Unique identifier for the item.
     * @param posX   Initial X position in the game world.
     * @param posY   Initial Y position in the game world.
     * @param width  Width for collision detection.
     * @param height Height for collision detection.
     * @param value  The positive score amount this item grants.
     * @param type   The category of the item (e.g., "COIN", "FUEL", "BOOST").
     */
    public Item(String id, double posX, double posY, int width, int height, int value, String type) {
        super(id, posX, posY, width, height);
        // Refactoring: Ensure value is non-negative to avoid logic errors
        this.value = Math.max(0, value);
        this.type = type;
    }

    /**
     * Applies the item's benefit to the given player.
     * Once applied, the item becomes invisible to prevent multiple collections.
     * * @param player The player who collected the item.
     */
    public void applyEffect(Player player) {
        if (player != null && this.visible) {
            player.addPoints(this.value);
            this.visible = false; // "Clean Code" fix: Immediate state update to prevent bugs
        }
    }

    /**
     * Renders the item based on its type and current visibility.
     */
    @Override
    public void draw() {
        if (this.visible) {
            // Rendering logic would be implemented here or delegated to a View class
        }
    }



    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}