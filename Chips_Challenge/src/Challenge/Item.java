package Challenge;

import javafx.scene.image.Image;

/**
 * An item is used to help a player navigate throughout the level.
 * It can be collected, has its own sprite and is either consumable
 * or is a permanent addition to the player's inventory.
 * @author Angelo
 * @version 1.0
 */
abstract class Item extends Entity {

    /**
     * Constructs an item with its designated sprite.
     * @param sprite The sprite used to render the item.
     */
    Item(Image sprite) {
        super(sprite);
    }

}