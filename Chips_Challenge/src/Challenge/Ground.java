package Challenge;

import javafx.scene.image.Image;

/**
 * This class represents a simple ground cell,
 * it is always passable by the player
 * @author George Carpenter
 * @version 1.0
 */
class Ground extends Cell {

    /**
     * The sprite used for this class
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/CELL_GROUND.png");
    }

    /**
     * Constructor
     */
    Ground() {
        super(SPRITE, true);
    }

}