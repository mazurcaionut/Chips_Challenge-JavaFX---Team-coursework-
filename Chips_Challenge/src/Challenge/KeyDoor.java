package Challenge;

import javafx.scene.image.Image;

/**
 * This class represents a Door in the game and can be unlocked by a
 * player carrying the correct Key, when it is replaced by a Ground cell
 * @author George Carpenter
 * @version 1.0
 */
class KeyDoor extends Door {

    /**
     * The colour of the door
     */
    private final Key.Colour colour;

    /**
     * Constructor
     * @param colour the colour of the door
     */
    KeyDoor(String colour) {
        super(new Image("images/CELL_KEY_DOOR_" + colour + ".png"));
        this.colour = setColour(colour);
    }

    /**
     * Gets the colour of the door
     * @return the colour of the door
     */
    Key.Colour getColour() {
        return this.colour;
    }

    /**
     * Sets the colour of the KeyDoor for checking
     * @param colour the colour to set
     * @return the colour Enum to set
     */
    private Key.Colour setColour(String colour) {

        if ("RED".equals(colour)) {
            return Key.Colour.RED;
        } else if ("GREEN".equals(colour)) {
            return Key.Colour.GREEN;
        } else if ("BLUE".equals(colour)) {
            return Key.Colour.BLUE;
        } else if ("PURPLE".equals(colour)) {
            return Key.Colour.PURPLE;
        }

        return null;
    }

}