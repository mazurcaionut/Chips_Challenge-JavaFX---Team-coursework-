package Challenge;

import javafx.scene.image.Image;

import java.util.Random;

/**
 * A dumb enemy is an enemy whose movement is determined by the players
 * location. It tries to find the shortest distance to the player and uses
 * that path to determine its next direction. It does not take into account
 * impassable territory hence it is a dumb enemy.
 * @author Samuel Roach, George Carpenter
 * @version 3.0
 */
class DumbEnemy extends Enemy {

    /**
     * The sprite to represent the dumb enemy.
     */
    private static final Image SPRITE;

    // private Lumberjack jack  = new Lumberjack();

    static {
        // Sets the Sprite to be rendered with this specific image.
        SPRITE = new Image("images/ENTITY_DUMB_ENEMY.png");
    }

    /**
     * Constructs a dumb enemy
     * @param position the position of the Enemy
     * @param direction The initial direction of the enemy
     */
    DumbEnemy(Position position, int direction) {
        super(SPRITE, position, direction);
    }

    /**
     * Gets the next direction of the Dumb Enemy.
     * @return The next direction.
     */
    int nextDirection(Level level) {
        Player player = level.getPlayer();
        return this.nextDirection(player);
    }

    /**
     * Finds the next direction of the dumb enemy where 0-3 represent North, East, South, West respectively.
     * @param player the player object
     * @return The next direction
     */
    private int nextDirection(Player player) {

        Random random = new Random();

        // Because this is hard to read aparently
        int north = 0;
        int east = 1;
        int south = 2;
        int west = 3;

        Position playerPos = player.getPosition();
        Position enemyPos = this.getPosition();

        if (playerPos.x == enemyPos.x || playerPos.y == enemyPos.y) {
            // Shared co-ordinate
            if (playerPos.x == enemyPos.x) {
                return playerPos.y > enemyPos.y ? south : north;
            } else {
                return playerPos.x > enemyPos.x ? east : west;
            }
        } else {
            // No shared co-ordinate
            if (playerPos.x > enemyPos.x) {
                return playerPos.y > enemyPos.y ?
                    // | true
                    random.nextBoolean() ? south : east :
                    // | otherwise
                    random.nextBoolean() ? north : east ;
            } else {
                return playerPos.y > enemyPos.y ?
                    // | true
                    random.nextBoolean() ? south : west :
                    // | otherwise
                    random.nextBoolean() ? north : west ;
            }
        }

    }

    /*

    For the above method; the following code will return the same result,
    I have included it here so whoever has to improve this in 235 can have a laugh.
    Side note: I actually prefer this to the nonsense above because it's more Haskell like.

        return playerPos.x == enemyPos.x || playerPos.y == enemyPos.y ?
            playerPos.x == enemyPos.x ?
                playerPos.y > enemyPos.y ?
                    south :
                    north :
                playerPos.x > enemyPos.x ?
                    east :
                    west :
            playerPos.x > enemyPos.x ?
                playerPos.y > enemyPos.y ?
                    random.nextBoolean() ?
                        south :
                        east :
                    random.nextBoolean() ?
                        north :
                        east :
                playerPos.y > enemyPos.y ?
                    random.nextBoolean() ?
                        south :
                        west :
                    random.nextBoolean() ?
                        north :
                        west;

    And on one line :D

    return playerPos.x == enemyPos.x || playerPos.y == enemyPos.y ? playerPos.x == enemyPos.x ? playerPos.y > enemyPos.y ? south : north : playerPos.x > enemyPos.x ? east : west : playerPos.x > enemyPos.x ? playerPos.y > enemyPos.y ? random.nextBoolean() ? south : east : random.nextBoolean() ? north : east : playerPos.y > enemyPos.y ? random.nextBoolean() ? south : west : random.nextBoolean() ? north : west;

     */

}
