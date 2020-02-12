package Challenge;

import javafx.scene.image.Image;

/**
 * Fire is an obstacle in game that,
 * if passed without fire boots will cause player death.
 * @author George Carpenter
 * @version 1.0
 */
class Fire extends Obstacle {

    /**
     * The sprite used for this class
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/CELL_FIRE.png");
    }

    /**
     * Constructs a Fire cell.
     */
    Fire() {
        super(SPRITE, false);
    }

}