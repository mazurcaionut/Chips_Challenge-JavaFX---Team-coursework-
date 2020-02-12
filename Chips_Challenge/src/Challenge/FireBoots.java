package Challenge;

import javafx.scene.image.Image;

/**
 * Fire boots allow the traversal of fire so long as they are in
 * the players inventory. That is, being an equipment, picking up fire
 * boots allows fire to be traversed for the rest of the level.
 * @author George Carpenter
 * @version 1.0
 */
class FireBoots extends Equipment {

    /**
     * The sprite used for this class
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/ENTITY_FIRE_BOOTS.png");
    }

    /**
     * Constructs a fire boot entity.
     */
    FireBoots() {
        super(SPRITE);
    }

}