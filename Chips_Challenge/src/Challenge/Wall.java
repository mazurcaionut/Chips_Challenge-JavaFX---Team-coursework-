package Challenge;

import javafx.scene.image.Image;

/**
 * This class represents a Wall Object in the game, it is never passable
 * by either the Player or Enemies.
 * @author George Carpenter
 * @version 1.0
 */
class Wall extends Cell {

    /**
     * The sprite used to represent a wall in the game
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/CELL_WALL.png");
    }

    /**
     * Constructor
     */
    Wall() {
        super(SPRITE, false);
    }

}