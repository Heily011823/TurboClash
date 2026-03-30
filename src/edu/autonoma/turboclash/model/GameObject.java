package edu.autonoma.turboclash.model;

/**
 * Base abstract class for all entities within the game world.
 * * Design Principles:
 * - Abstraction: Defines common behavior (physics/collision) without enforcing a specific visual.
 * - Liskov Substitution Principle (LSP): Any GameObject subtype can be checked for collisions
 * using the same logic.
 */
public abstract class GameObject {
    protected final String id;
    protected double posX;
    protected double posY;
    protected final int width;
    protected final int height;
    protected boolean visible;

    /**
     * Constructs a game object with specified dimensions and position.
     * * @param id     Unique entity identifier.
     * @param posX   Initial X coordinate.
     * @param posY   Initial Y coordinate.
     * @param width  Object width in pixels.
     * @param height Object height in pixels.
     */
    public GameObject(String id, double posX, double posY, int width, int height) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.visible = true;
    }

    /**
     * Abstract method to be implemented by subclasses to handle rendering.
     * This enforces the "Single Responsibility" of each object knowing its visual representation.
     */
    public abstract void draw();

    /**
     * Determines if this object is overlapping with another game object.
     * Uses AABB (Axis-Aligned Bounding Box) collision detection algorithm.
     * * @param other The other GameObject to check against.
     * @return true if a collision is detected.
     */
    public boolean collidesWith(GameObject other) {
        if (other == null || !this.visible || !other.visible) {
            return false;
        }

        return this.posX < other.posX + other.width &&
                this.posX + this.width > other.posX &&
                this.posY < other.posY + other.height &&
                this.posY + this.height > other.posY;
    }


    public String getId() { return id; }
    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}