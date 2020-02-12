package Challenge;

import javafx.scene.image.Image;

/**
 * This is a cell class representing an abstract cell in the game.
 * Not meant to be instantiated, it is used to allow all cells to be able
 * to set and get its type and also indicate whether it is a passable cell or not.
 * @author George Carpenter and Angelo Balistoy
 * @version 1.0
 */
abstract class Cell {

    /**
     * Whether the cell is passable by movable entities.
     */
    private boolean passable;

    /**
     * Sprite used to render the object in game.
     */
    private final Image SPRITE;

//
//             ___
//         _(((,|    What's DNA??
//        /  _-\\
//       / C o\o \    it's called deoxyribonucleic acid you dingus!
//     _/_    __\ \     __ __     __ __     __ __     __
//    /   \ \___/  )   /--X--\   /--X--\   /--X--\   /--
//    |    |\_|\  /   /--/ \--\ /--/ \--\ /--/ \--\ /--/
//    |    |#  #|/          \__X__/   \__X__/   \__X__/
//    (   /     |
//     |  |#  # |
//     |  |    #|
//     |  | #___n_,_
//  ,-/   7-' .     `\
//  `-\...\-_   -  o /
//     |#  # `---U--'
//     `-v-^-'\/
//       \  |_|_
//       (______)
//
//  Figure VII - Not a Cell because I couldn't find one
//

    /**
     * Constructs a cell object.
     * @param sprite the sprite used to represent this Cell
     * @param passable whether or not this Cell is passable
     */
    Cell(Image sprite, boolean passable) {
        this.SPRITE = sprite;
        this.passable = passable;
    }

    /**
     * Gets the sprite the object is using.
     * @return The sprite being used to render the file.
     */
    Image getSprite() {
        return SPRITE;
    }

    /**
     * Changes a cells Passable state
     */
    void setPassable() {
        this.passable = true;
    }

    /**
     * Checks if the object is passable.
     * @return The object's passable state.
     */
    boolean isPassable() {
        return this.passable;
    }

}