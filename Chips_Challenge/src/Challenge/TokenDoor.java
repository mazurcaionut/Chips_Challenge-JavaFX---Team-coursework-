package Challenge;

import javafx.scene.image.Image;

/**
 * This class represents a Token Door which requires a set number of Tokens
 * to unlock at which point it will be replaced by a Ground Cell.
 * @author George Carpenter
 * @version 1.0
 */
class TokenDoor extends Door {

    /**
     * The sprite used for this class
     */
    private static final Image SPRITE;

    /**
     * How many keys are required to open the door
     */
    private final int requirement;

    static {
        SPRITE = new Image("images/CELL_TOKEN_DOOR.png");
    }

    /**
     * Constructor
     * @param requirement number of tokens required to open the door
     */
    TokenDoor(int requirement) {
        super(SPRITE);
        this.requirement = requirement;
    }

    /**
     * Returns the requirement for the door
     * @return the requirement for the door
     */
    int getRequirement() {
        return this.requirement;
    }

}