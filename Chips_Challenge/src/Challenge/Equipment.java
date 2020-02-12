package Challenge;

import javafx.scene.image.Image;

/**
 * An equipment is a items that is permanently in a players inventory,
 * allowing the player to traverse through hazards like fire.
 * @author Angelo
 * @version 1.0
 */
abstract class Equipment extends Item {

    /**
     * Constructs a equipment object.
     * @param sprite The sprite for that specific equipment.
     */
    Equipment(Image sprite) {
        super(sprite);
    }

//
//       /
//  O===[====================-
//      \
//
//  Figure V - Engarde!

}