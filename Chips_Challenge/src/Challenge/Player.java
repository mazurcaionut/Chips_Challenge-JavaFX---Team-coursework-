package Challenge;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * This class represents the Player object which will be controlled by the
 * User, it is able to collect move around the game ane interact with both
 * Items and Cells within the level.
 * @author George Carpenter
 * @version 1.0
 */
class Player extends Entity {

    /**
     * The Sprite used for the Player object,
     * it will be rotated based on direction
     */
    private static Image SPRITE;

    /**
     * An array list used as the Player Inventory,
     * items will be added and removed during game play
     */
    private ArrayList<Item> inventory;

    /**
     * The Players Position Object
     */
    private Position position;

    /**
     * The direction we should be displaying the player spite in
     */
    private int direction;

    /**
     * The direction the Player is facing;
     */
    private int facing;

    /**
     * How many Tokens the Player is currently carrying
     */
    private int tokenCount;

    /**
     * Tracks whether or not the Player has been killed
     */
    private boolean alive;

    /**
     * Tracks if the game is finished
     */
    private boolean finish;

    // private final Lumberjack jack = new Lumberjack();

    static {
        SPRITE = new Image("images/ENTITY_PLAYER_1.png");
    }

    /**
     * Constructs a Player object
     * @param position the position of the Player
     * @param direction the direction the player is facing
     */
    Player(Position position, int direction) {
        super(SPRITE);
        this.position = position;
        this.inventory = new ArrayList<>();
        this.direction = direction;
        this.facing = 1;
        this.tokenCount = 0;
        this.alive = true;
        this.finish = false;
    }

    /**
     * Gets the Player inventory, for printing to screen
     * @return the player inventory
     */
    ArrayList<Item> getInventory() {
        return inventory;
    }

    /**
     * Gets the Player's Position
     * @return the Player's Position object
     */
    Position getPosition() {
        return this.position;
    }

    /**
     * Used to track the current direction
     * @return the Players direction
     */
    int getDirection() {
        return this.direction;
    }

    /**
     * Gets the direction the Player is facing
     * @return the direction the Player is Facing
     */
    int getFacing() {
        return this.facing;
    }

    /**
     * USed to store how many tokens the player has
     * @return the number of tokens held
     */
    int getTokenCount() {
        return this.tokenCount;
    }

    /**
     * Holds the Players alive status
     * @return true if they are alive
     */
    boolean getStatus() {
        return this.alive;
    }

    /**
     * Used to track if the game is complete
     * @return true if complete
     */
    boolean getGameStatus() {
        return this.finish;
    }

    /**
     * Used to set the Player Token count
     * @param count the count to set it at
     */
    void setTokenCount(int count) {
        this.tokenCount = count;
    }

    /**
     * Resets the game status
     */
    void setGameStatus() {
        this.finish = false;
    }

    /**
     * Used to move the Player in the Entity grid
     * @param direction the direction the Player wishes to move
     * @param level the Level object in which they are moving
     * @return the updated Entity grid for displaying to the screen
     */
    Entity[][] move(int direction, Level level) {

        Position p = this.position;

        if (0 == direction) {
            p = new Position(p.x, p.y - 1);
        } else if (1 == direction) {
            p = new Position(p.x + 1, p.y);
        } else if (2 == direction) {
            p = new Position(p.x, p.y + 1);
        } else if (3 == direction) {
            p = new Position(p.x - 1, p.y);
        }

        this.direction = direction;

        if (1 == this.direction || 3 == this.direction) {
            this.facing = this.direction;
        }

        if (checkValidMove(level, p)) {
            return movePlayerEntity(level, p);
        } else {
            //Lumberjack.log(1, "Invalid Player Move");
            return level.getEntityGrid();
        }

    }

    /**
     * Used to check if a Player Move is valid before carrying it out
     * @param level the level Object the Player is moving in
     * @param p the position they wish to move to
     * @return whether or not it is a valid move
     */
    private boolean checkValidMove(Level level, Position p) {

        Cell[][] cellGrid = level.getCellGrid();

        int width = cellGrid.length - 1;
        int height = cellGrid[0].length - 1;

        return p.x >= 0 && p.y >= 0 && p.x <= width && p.y <= height;
    }

    /**
     * Used to move the player object in the Entity grid
     * @param level the current Level object
     * @param next the next Position of the player, possibly
     * @return the updated Entity grid for displaying to the screen
     */
    private Entity[][] movePlayerEntity(Level level, Position next) {

        Cell[][] cellGrid = level.getCellGrid();
        Entity[][] entityGrid = level.getEntityGrid();

        Cell cell = cellGrid[next.x][next.y];
        Entity entity = entityGrid[next.x][next.y];

        if (null != entity) {

            if (entity instanceof Item) {
                this.addItem(level, (Item) entityGrid[next.x][next.y]);
                // jack.log("FOUND ITEM + this.inventory.toString());
            } else if (entity.getClass().getSimpleName().contains("Enemy")) {
                killPlayer();
                return entityGrid;
            }

        } else if (cell instanceof KeyDoor) {

            if (null != findKey(((KeyDoor) cell).getColour())) {
                openKeyDoor(level, ((KeyDoor) cell).getColour(), next.x, next.y);
            } else {
                return entityGrid;
            }

        } else if (cell instanceof TokenDoor) {

            if (this.tokenCount >= ((TokenDoor) cell).getRequirement()) {
                openTokenDoor(level, (TokenDoor) cell, next.x, next.y);
            } else {
                return entityGrid;
            }

        } else if (cell instanceof Teleporter) {

            Teleporter pair = ((Teleporter) cell).getPair();

            int[] pairLocation = level.getLocation(cellGrid, pair);
            next = new Position(pairLocation[0], pairLocation[1]);

        } else if (cell instanceof Goal) {
            this.finish = true;
        } else if (!cell.isPassable()) {

            String cellName = cell.getClass().getSimpleName();

            if ("Water".equals(cellName) || "Fire".equals(cellName)) {
                killPlayer();
            }

            return entityGrid;
        }

        // Remove the player from the grid
        entityGrid[this.position.x][this.position.y] = null;

        // Add the player at new location
        entityGrid[next.x][next.y] = this;

        // Update the Players Position
        this.position = new Position(next.x, next.y);

        return entityGrid;
    }

//
//                      _______
//             ..-'`       ````---.
//           .'          ___ .'````.'SS'.
//          /        ..-SS####'.  /SSHH##'.
//         |       .'SSSHHHH##|/#/#HH#H####'.
//        /      .'SSHHHHH####/||#/: \SHH#####\
//       /      /SSHHHHH#####/!||;`___|SSHH###\
//    -..__    /SSSHHH######.         \SSSHH###\
//    `.'-.''--._SHHH#####.'           '.SH####/
//      '. ``'-  '/SH####`/_             `|H##/
//      | '.     /SSHH###|`'==.       .=='/\H|
//      |   `'-.|SHHHH##/\__\/        /\//|~|/
//      |    |S#|/HHH##/             |``  |
//      |    \H' |H#.'`              \    |
//      |        ''`|               -     /
//      |          /H\          .----    /
//      |         |H#/'.           `    /
//      |          \| | '..            /
//      |            /|    ''..______.'
//       \          //\__    _..-. |
//        \         ||   ````     \ |_
//         \    _.-|               \| |_
//         _\_.-'   `'''''-.        |   `--.
//     ''``    \            `''-;    \ /
//              \      .-'|     ````.' -
//              |    .'  `--'''''-.. |/
//              |  .'               \|
//              |.'
//
//    Figure IIX? I think - A Player!
//

    /**
     * Used to add items to the Players inventory
     * This Method can also update some cells to passable
     * @param level the Level object, if it needs updating as a result
     * @param item the item to add to the players inventory
     */
    void addItem(Level level, Item item) {

        if (item instanceof Token) {

            this.tokenCount += 1;

            // jack.log(1, "Current tokens: " + tokenCount);

            if (checkTokenInInv()) {
                return;
            }

        } else if (item instanceof FireBoots || item instanceof Flippers) {

            setCellsPassable(level, item);

        }

        inventory.add(item);
    }

    /**
     * Method to 'open' a key door and replace it with ground
     * @param level the level object
     * @param doorColour the colour of door
     * @param newX the X location of the door
     * @param newY the Y location of the door
     */
    private void openKeyDoor(Level level, Key.Colour doorColour, int newX, int newY) {

        Cell[][] cellGrid = level.getCellGrid();

        // jack.log(1, "Player has the correct key");

        this.inventory.remove(findKey(doorColour));

        cellGrid[newX][newY] = new Ground();

        level.setCellGrid(cellGrid);

    }

    /**
     * Method to find a key of a set colour
     * @param colour the colour to search for
     * @return the key, if present
     */
    private Item findKey(Key.Colour colour) {

        for (Item item : this.inventory) {
            if (item instanceof Key) {

                Key currentKey = (Key) item;

                // System.out.println(currentKey.getColour().toString());

                if (colour.toString().equals(currentKey.getColour().toString())) {
                    return item;
                }

            }
        }

        return null;
    }

    /**
     * Method to 'open' a token door and replace it with ground
     * @param level the level object
     * @param door the door object
     * @param newX the X location of the door
     * @param newY the Y location of the door
     */
    private void openTokenDoor(Level level, TokenDoor door, int newX, int newY) {

        Cell[][] cellGrid = level.getCellGrid();

        // jack.log(1,"Opening token door");

        cellGrid[newX][newY] = new Ground();

        level.setCellGrid(cellGrid);

        // this.removeTokens(door.getRequirement());

    }

    /**
     * Checks if a Player is already carrying a token
     * @return true if a token exists
     */
    private boolean checkTokenInInv() {

        for (Item item : inventory) {

            if (item instanceof Token) {
                return true;
            }

        }

        return false;
    }

    /**
     * Method to set cells as passable on item pickup
     * @param level the level object
     * @param item the item collected, this dictates what is made passable
     */
    private void setCellsPassable(Level level, Item item) {

        Cell[][] cellGrid = level.getCellGrid();

        String cellType = item instanceof FireBoots ? "Fire" : "Water";

        for (Cell[] row : cellGrid) {
            for (Cell c : row) {
                if (cellType.equals(c.getClass().getSimpleName())) {
                    c.setPassable();
                }
            }
        }

        // jack.log(1, cellType + " set to passable");

    }

    /**
     * Kills the Player
     */
    private void killPlayer() {
        this.alive = false;
    }

}