package Challenge;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The MiniMap is a mini canvas that will be visible during the game and show
 * the player their surroundings sans entitys
 * @author Ioan Mazurca, George Carpenter
 * @version 2.0
 */
class MiniMap {

    /**
     * The width of cells in the MiniMap, calculated dynamically
     */
    private static int MINIMAP_CELL_WIDTH;

    /**
     * The height of cells in the MiniMap, calculated dynamically
     */
    private static int MINIMAP_CELL_HEIGHT;

    /**
     * Used to draw the mini map to the screen
     * @param level the level object to draw from
     * @param canvas the canvas to draw too
     */
    void drawMap(Level level, Canvas canvas) {

        // Because it's logical
        assert null != level;

        int xMap = level.getCellGrid().length - 1;
        int yMap = level.getCellGrid()[xMap].length - 1;

        MINIMAP_CELL_WIDTH = (int) canvas.getWidth() / xMap;
        MINIMAP_CELL_HEIGHT = (int) canvas.getHeight() / yMap;

        // Get the Graphic Context of the canvas. This is what we draw on.
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Clear canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Render stuff
        renderCellGrid(gc, level.getCellGrid());
        renderPlayer(gc, level.getPlayer());

    }

    /**
     * Used to render the cell grid for the mini map
     * @param gc the graphics context used to draw
     * @param cellGrid the cell grid to draw
     */
    private void renderCellGrid(GraphicsContext gc, Cell[][] cellGrid) {

        for (int x = 0 ; x < cellGrid.length ; x++ ) {
            for (int y = 0 ; y < cellGrid[x].length ; y++ ) {

                Cell cell = cellGrid[x][y];
                Image sprite = SpriteConverter.resize(cell.getSprite(), MINIMAP_CELL_HEIGHT, MINIMAP_CELL_WIDTH);

                int xOnScreen = x * MINIMAP_CELL_WIDTH;
                int yOnScreen = y * MINIMAP_CELL_HEIGHT;

                gc.drawImage(sprite, xOnScreen, yOnScreen);

            }
        }

    }

    /**
     * Used to render the player location on the mini map
     * @param gc the graphics context used to draw
     * @param player the player object to draw from
     */
    private void renderPlayer(GraphicsContext gc, Player player) {

        Position playerPosition = player.getPosition();

        int fac = player.getFacing();

        String filePath = "images/ENTITY_PLAYER_";
        String fileExt = ".png";

        Image sprite = new Image(filePath + fac + fileExt);

        int xOnScreen = playerPosition.x * MINIMAP_CELL_WIDTH;
        int yOnScreen = playerPosition.y * MINIMAP_CELL_HEIGHT;

        sprite = SpriteConverter.resize(sprite, MINIMAP_CELL_HEIGHT, MINIMAP_CELL_WIDTH);

        gc.drawImage(sprite, xOnScreen, yOnScreen);

    }

}
