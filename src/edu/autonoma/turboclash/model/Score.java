package edu.autonoma.turboclash.model;

/**
 * Manages the player's numerical performance within the game.
 * * Design Principles:
 * - Encapsulation: Prevents direct access to the 'points' variable, ensuring
 * data integrity through controlled methods.
 * - Single Responsibility (SRP): Exclusively handles point-based logic and
 * score boundary rules.
 */
public class Score {
    private int points;

    // Constant to avoid hard-coded values (Magic Numbers)
    private static final int MIN_POINTS = 0;

    /**
     * Initializes the score at the default starting value.
     */
    public Score() {
        this.points = MIN_POINTS;
    }

    /**
     * Increases the current score by a specific amount.
     * * @param value Positive integer to add to the score.
     */
    public void increment(int value) {
        if (value > 0) {
            this.points += value;
        }
    }

    /**
     * Decreases the current score by a specific amount.
     * Refactoring: Implements a floor limit to prevent negative scores.
     * * @param value Positive integer to subtract from the score.
     */
    public void decrement(int value) {
        if (value > 0) {
            this.points -= value;
            // Rule: Score cannot drop below the minimum defined (0)
            if (this.points < MIN_POINTS) {
                this.points = MIN_POINTS;
            }
        }
    }

    /**
     * General update method to handle both positive and negative changes.
     * Useful for synchronization and centralized logic.
     * * @param amount The delta value to apply to the current score.
     */
    public void update(int amount) {
        if (amount > 0) {
            increment(amount);
        } else if (amount < 0) {
            decrement(Math.abs(amount));
        }
    }

    /**
     * Retrieves the current score value.
     * * @return The current total points.
     */
    public int getPoints() {
        return points;
    }
}