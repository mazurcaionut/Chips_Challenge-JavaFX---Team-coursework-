package Challenge;

import javafx.scene.image.Image;

import java.util.stream.IntStream;

/**
 * Enemies are movable hazards designed to end the level upon contact
 * with the player. Each enemy has its own unique way of moving to the player.
 * Again this class shouldn't be instantiated, instead, its sub-classes should be.
 * @author George Carpenter, Angelo Balistoy
 * @version 1.0
 */
abstract class Enemy extends Entity {

    /**
     * The direction the enemy will travel
     */
    private int direction;

    /**
     * The cell grid the enemy uses
     */
    private Cell[][] cellGrid;

    /**
     * The entity grid the enemy uses
     */
    private Entity[][] entityGrid;

    /**
     * The position of the Enemy
     */
    private Position position;

    /**
     * Creates an enemy
     * @param sprite the sprite used to represent this Entity
     * @param position the position of the Enemy
     * @param direction the direction the enemy is set upon creation
     * */
    Enemy(Image sprite, Position position, int direction) {
        super(sprite);
        this.position = position;
        this.direction = direction;
    }

    /**
     * Gets the direction.
     * @return A number from 0-3 (North South East West).
     */
    int getDirection() {
        return direction;
    }

    /**
     * Gets the Position of this Enemy
     * @return the Enemy Position Object
     */
    Position getPosition() {
        return this.position;
    }

    /**
     * Gets the cell grid.
     * @return The cell grid.
     */
    protected Cell[][] getCellGrid() {
        return this.cellGrid;
    }

    /**
     * Sets the direction of the enemy.
     * @param direction The new direction of the enemy.
     */
    void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Sets the cell grid for the enemy.
     * @param cellGrid The cell grid to be used.
     */
    private void setCellGrid(Cell[][] cellGrid) {
        this.cellGrid = cellGrid;
    }

    /**
     * Sets the entity grid for the enemy.
     * @param entityGrid The cell grid to be used.
     */
    private void setEntityGrid(Entity[][] entityGrid) {
        this.entityGrid = entityGrid;
    }

    /**
     * Gets the next direction of enemies.
     * @param level The level being used.
     * @return The next direction from 0-3 representing North, East, South, West respectively.
     */
    abstract int nextDirection(Level level);

    /**
     * Moves the enemy based on a given direction.
     * @param level The current level.
     * @param entityGrid the entityGrid in which to move the Enemy Object
     * @return The new entity grid to be used in the next turn.
     */
    Entity[][] move(Level level, Entity[][] entityGrid) {

        Position position = getPosition();

        int newX = position.x;
        int newY = position.y;

        // For all instances of enemy, use its specific next direction.
        int direction = nextDirection(level);

        if (0 == direction) {
            newY = position.y - 1;
        } else if (1 == direction) {
            newX = position.x + 1;
        } else if (2 == direction) {
            newY = position.y + 1;
        } else if (3 == direction) {
            newX = position.x - 1;
        }

        if (this.getCells()[direction]) {
            // If they can actually move there

            // Move the Enemy Object
            this.position = new Position(newX, newY);
            entityGrid[newX][newY] = this;

            // Clear the old position in the grid
            entityGrid[position.x][position.y] = null;

        }

        // Return the new grid
        return entityGrid;
    }

//
//                                  __
//                         /\    .-" /
//                        /  ; .'  .'
//                       :   :/  .'
//                        \  ;-.'
//           .--""""--..__/     `.
//         .'           .'    `o  \
//        /                    `   ;
//       :                  \      :
//     .-;        -.         `.__.-'
//    :  ;          \     ,   ;
//    '._:           ;   :   (
//        \/  .__    ;    \   `-.
//         ;     "-,/_..--"`-..__)
//         '""--.._:
//
//  Figure I, the Killer Rabbit of Caerbannog
//

    /**
     * Gets a list of possible moves for the Enemy
     * @return a list of possible moves, not all are available
     */
    boolean[] getCells() {

        int x = this.position.x;
        int y = this.position.y;

        boolean[] passable = new boolean[4];

        // System.out.println(this.getClass().getSimpleName());

        Cell[] sc = {
            this.cellGrid[x][y - 1],
            this.cellGrid[x + 1][y],
            this.cellGrid[x][y + 1],
            this.cellGrid[x - 1][y]
        };

        Entity[] se = {
            this.entityGrid[x][y - 1],
            this.entityGrid[x + 1][y],
            this.entityGrid[x][y + 1],
            this.entityGrid[x - 1][y]
        };

        IntStream.range(0, passable.length).forEach(i ->
            passable[i] = sc[i] instanceof Ground &&
            (se[i] == null || se[i] instanceof Player));

        return passable;

    }

    /**
     * Used to update both grids at once
     * @param cellGrid the new Cell grid
     * @param entityGrid the nes Entity grid
     */
    void updateGrids(Cell[][] cellGrid, Entity[][] entityGrid) {
        setCellGrid(cellGrid);
        setEntityGrid(entityGrid);
    }

}
