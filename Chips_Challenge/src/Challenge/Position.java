package Challenge;

/**
 * Position is a class used to represent a kind of Tuple which will
 * hold position values for movable Entitys during the game
 * @author George Carpenter
 * @version 1.0
 */
class Position {

    /**
     * X location
     */
    final int x;

    /**
     * Y location
     */
    final int y;

    /**
     * Constructs a position
     * @param x X location of the Entity
     * @param y Y location of the Entity
     */
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
