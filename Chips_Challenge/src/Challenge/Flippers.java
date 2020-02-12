package Challenge;

import javafx.scene.image.Image;

/**
 * Flippers allow the traversal of water so long as they are in the
 * players inventory. That is, being an equipment, picking up flippers
 * allows water to be traversed for the rest of the level.
 * @author George Carpenter
 * @version 1.0
 */
class Flippers extends Equipment {

    /**
     * The sprite used for the flipper
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/ENTITY_FLIPPERS.png");
    }

    /**
     * Constructs a flipper entity.
     */
    Flippers() {
        super(SPRITE);
    }

}