package Challenge;

import javafx.scene.image.Image;

/**
 * Water is an obstacle in game that,
 * if passed without flippers will cause player death.
 * @author George Carpenter
 * @version 1.0
 */
class Water extends Obstacle {

    /**
     * The sprite used for this class
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/CELL_WATER.png");
    }

    /**
     * Constructor
     */
    Water() {
        super(SPRITE, false);
    }

}