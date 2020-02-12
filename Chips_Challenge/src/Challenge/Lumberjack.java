package Challenge;

/**
 * This class is used entirely for testing, although we may also submit it
 * @author George Carpenter
 * @version 1.0
 */
class Lumberjack {

    /*
     * They're a lumberjack and they're OK
     * They sleep all night and they work all day
     * https://www.youtube.com/watch?v=FshU58nI0Ts
     */

    /**
     * Logs stuff
     * @param message what to log
     */
    private static void log(String message) {
        System.out.println(message);
    }

    /**
     * Logs more stuff
     * @param priority for not spam
     * @param message what to log
     */
    static void log(int priority, String message) {

        if (0 == priority) {
            log("Spam : " + message);
        } else if (1 == priority) {
            log("Not Spam : " + message);
        } else if (2 == priority) {
            log("Maybe useful : " + message);
        } else {
            log(message);
        }

    }

    /**
     * Logs a grid
     * @param grid the grid to log
     * @param <T> Cell or Entity
     */
    <T> void logGrid(T[][] grid) {

        System.out.println("===== START GRID =====");

        for (T[] row : grid) {
            for (T e : row) {

                if (null == e) {
                    log("NULL");
                } else {
                    log(e.getClass().getSimpleName());
                }
            }
        }

    }

    /**
     * Logs the Player location
     * @param player the Player object to track
     * @param entityGrid the grid to find them in
     */
    void logPlayerLoc(Player player, Entity[][] entityGrid) {

        Position playerPosition = player.getPosition();

        int direction = player.getDirection();

        log(2, "Player Loc + Dir -> " + playerPosition.x + " " + playerPosition.y + " " + direction);

    }

}
