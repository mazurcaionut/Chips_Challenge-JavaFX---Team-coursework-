package Challenge;

import javafx.scene.image.Image;

/**
 * The Line Enemy is an enemy that will only ever travel in one of two
 * directions, its initial direction or its cardinal opposite, these will
 * change dynamically whenever it comes across an impassable cell.
 * @author George Caprenter, Ioan Mazurca
 * @version 3.0
 */
class LineEnemy extends Enemy {

    /**
     * The sprite to represent the dumb enemy.
     */
    private static final Image SPRITE;

    static {
        SPRITE = new Image("images/ENTITY_LINE_ENEMY.png");
    }

    /**
     * Constructs a Line Enemy Entity
     * @param position the position of the Line Enemy
     * @param direction the initial direction of the Line Enemy
     */
    LineEnemy(Position position, int direction) {
        super(SPRITE, position, direction);
    }

    /**
     * Gets the next direction of the Line Enemy
     * @return The next direction.
     */
    public int nextDirection(Level level) {
        return this.nextDirection();
    }

    /**
     * Used to calculate the next direction for the line enemy
     * @return the next direction
     */
    private int nextDirection() {

        // This method is basically magic, do not question it's technique!

        boolean[] passable = getCells();
        int dir = getDirection();

        if (!passable[dir]) {
            setDirection((dir + 2) % 4);
        }

        return getDirection();
    }

}