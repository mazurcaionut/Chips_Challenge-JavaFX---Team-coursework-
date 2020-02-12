package Challenge;

import javafx.scene.image.Image;

/**
 * An entity is an object unrelated to the environment to the game.
 * That is, some enemies, such as items, are able to be picked up by the
 * player. However items are in of itself not a Cell designed to be the
 * foundations of the level. Other objects are classified as an entity
 * such as player or enemies. All of them have a sprite to represent them.
 * @author George Carpenter
 * @version 1.0
 */
abstract class Entity {

    /**
     * The sprite used to represent objects of this type in the game
     */
    private final Image SPRITE;

    /**
     * Constructor for an Entity
     * @param sprite the sprite passed from the sub class
     */
    Entity(Image sprite) {
        this.SPRITE = sprite;
    }

    /**
     * Returns the sprite to be rendered in the game
     * @return the sprite for rendering
     */
    Image getSprite() {
        return SPRITE;
    }

}