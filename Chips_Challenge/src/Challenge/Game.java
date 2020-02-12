package Challenge;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Game is used to render Chips Challenge.
 * We render the entire level with this class.
 * @author Everyone basically
 * @version 9001
 */
class Game {

    /**
     * The cell Size.
     */
    private static final int GRID_SIZE = 120;

    /**
     * The save file for this game.
     */
    private final Save save = new Save();

    /**
     * The current user for this game
     */
    private User user;

    /**
     * Constructs a Game
     * @param userName the name of the user playing
     */
    Game(String userName) {
        this.user = new User(userName);
        user.setScores(save.loadPlayerScores(userName));
        //jack.log(1, user.getScores().toString());
    }

    /**
     * Gets the user name, for saving
     * @return their name
     */
    public User getUser() {
        return user;
    }

    /**
     *  Draws the game with a given level and canvas.
     * @param level The game's level
     * @param canvas The canvas to draw the game.
     */
    void drawGame(Level level, Canvas canvas) {

        // Because it's logical
        assert null != level;

        // Get the Graphic Context of the canvas. This is what we draw on.
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Clear canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Calculate the offset for use in rendering
        Position offset = this.calculateOffSet(level, canvas);

        // Render stuff
        this.renderBackground(gc, canvas);
        this.renderCellGrid(gc, level.getCellGrid(), offset);
        this.renderEntityGrid(gc, level.getEntityGrid(), offset);

        // Lumberjack.log(2,"game" + this.user.getUserName());
        save.saveFile(level, this.user);

        // Log Stuff - uncomment for spam
        // Lumberjack.logPlayerLoc(player, entityGrid);
        // Lumberjack.logGrid(level.getEntityGrid());
        // Lumberjack.logGrid(level.getCellGrid());

    }

    /**
     * Calculates the offset for when you draw the game.
     * @param level The current level.
     * @param canvas The canvas used for the game.
     * @return The offset for the x coordinate and y coordinate as a pair represented by a int[].
     */
    private Position calculateOffSet(Level level, Canvas canvas) {

        // Not 100% sure of this, it may change, please don't try to comment it

        Player player = level.getPlayer();
        Position playerPosition = player.getPosition();

        int playerXOffset = playerPosition.x * GRID_SIZE + (GRID_SIZE / 2);
        int playerYOffset = playerPosition.y * GRID_SIZE + (GRID_SIZE / 2);

        int levelXOffset = playerXOffset - (int) canvas.getWidth() / 2;
        int levelYOffset = playerYOffset - (int) canvas.getHeight() / 2;

        return new Position(levelXOffset, levelYOffset);

    }

    /**
     * Renders the background for the game.
     * @param gc The graphics context for rendering.
     * @param canvas The canvas for the game.
     */
    private void renderBackground(GraphicsContext gc, Canvas canvas) {

        Image backing = new Image("images/BACKING.png");
        backing = SpriteConverter.resize(backing, (int) canvas.getHeight(), (int) canvas.getWidth());
        gc.drawImage(backing, 0, 0);

        // gc.drawImage(SpriteConverter.resize(new Image("images/BACKING.png"), (int) canvas.getHeight(), (int) canvas.getWidth()), 0, 0);

    }

    /**
     * Renders the cell grid.
     * @param gc The graphics context for rendering.
     * @param cellGrid The cell grid for the game to be rendered.
     * @param offset The offset needed to be taken into account.
     */
    private void renderCellGrid(GraphicsContext gc, Cell[][] cellGrid, Position offset) {

        int xOnScreen;
        int yOnScreen;

        for (int x = 0 ; x < cellGrid.length ; x++ ) {
            for (int y = 0 ; y < cellGrid[x].length ; y++ ) {

                Cell cell = cellGrid[x][y];
                Image sprite = SpriteConverter.resize(cell.getSprite(), GRID_SIZE, GRID_SIZE);

                xOnScreen = x * GRID_SIZE - offset.x;
                yOnScreen = y * GRID_SIZE - offset.y;

                gc.drawImage(sprite, xOnScreen, yOnScreen);

            }
        }

    }

    /**
     * Renders the entities in the game.
     * @param gc The graphics context for the game.
     * @param entityGrid The entities to be rendered.
     * @param offset The offset for the entities.
     */
    private void renderEntityGrid(GraphicsContext gc, Entity[][] entityGrid, Position offset) {

        int xOnScreen;
        int yOnScreen;

        Position position;

        for (int x = 0 ; x < entityGrid.length ; x++ ) {
            for (int y = 0; y < entityGrid[x].length; y++ ) {

                if (null != entityGrid[x][y]) {

                    xOnScreen = x * GRID_SIZE - offset.x;
                    yOnScreen = y * GRID_SIZE - offset.y;

                    position = new Position(xOnScreen, yOnScreen);

                    renderEntity(gc, entityGrid[x][y], position);
                }

            }
        }
    }

    /**
     * Auxiliary method to help render the entitys
     * @param gc The graphics context to be used in the game.
     * @param entity The entity to be rendered.
     * @param position The position of the enemy.
     */
    private void renderEntity(GraphicsContext gc, Entity entity, Position position) {

        Image sprite = entity.getSprite();

        if (entity instanceof Player) {

            sprite = renderPlayer((Player) entity);

        } else if (entity instanceof Enemy) {

            Enemy enemy = (Enemy) entity;

            sprite = enemy.getSprite();

            sprite = SpriteConverter.rotate(sprite, enemy.getDirection());
        }

        sprite = SpriteConverter.resize(sprite, GRID_SIZE, GRID_SIZE);
        gc.drawImage(sprite, position.x, position.y);

    }

    private Image renderPlayer(Player player) {

        int fac = player.getFacing();

        String filePath = "images/ENTITY_PLAYER_";
        String fileExt = ".png";

        Image sprite = new Image(filePath + fac + fileExt);

        return sprite;
    }

}
