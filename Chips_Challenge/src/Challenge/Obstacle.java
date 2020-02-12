package Challenge;

import javafx.scene.image.Image;

/**
 * This is an abstract class designed to represent Obstacle Cells in the game
 * that may start out impassable but at some point may become passable by the
 * player through the collection of Items and Equipment.
 * @author George Carpenter
 * @version 1.0
 */
abstract class Obstacle extends Cell {

    /**
     * Constructor
     * @param sprite the sprite used to represent an object of this type
     * @param passable whether or not this cell will be passable
     */
    Obstacle(Image sprite, boolean passable) {
        super(sprite, passable);
    }

}