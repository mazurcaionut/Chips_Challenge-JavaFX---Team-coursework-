package Challenge;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**A level contains all information regarding the fundamental aspects of the game. That is, it holds information on all
 * cells and entities in the entire game. These aspects can be manipulated in this class to allow the level to change
 * accordingly due to entity actions.
 * This class acts as a kind of super-ish class for the game, holding all
 * information about the grids and allowing the "game" to run. The player,
 * and enemies, will always be interacting with one or other part of it.
 * @author George Carpenter
 * @version 2.0
 */
class Level {

    /**
     * The cell grid to build
     */
    private static Cell[][] cellGrid;

    /**
     * The entity grid to build
     */
    private static Entity[][] entityGrid;

    /**
     * The name of the level to build
     */
    private final String levelName;

    /**
     * The list of Enemies in the Level, can be empty
     */
    private final ArrayList<Enemy> enemies;
    //private Lumberjack jack = new Lumberjack();

    /**
     * Constructs a Level from a given file name
     * @param levelName the name of the level to build
     */
    Level(String levelName) {

        this.levelName = levelName;
        this.enemies = new ArrayList<>();
        //jack.log(1, this.levelName);

        try {
            buildLevel(levelName);
        } catch (FileNotFoundException e) {

            // e.printStackTrace();

            // This won't ever be caught because the buttons in
            // the game will only load files that exist already
            // and as such no FileNotFound can ever occur

        }

    }

    /**
     * Gets the cell grid
     * @return the cell grid off the Level object
     */
    public Cell[][] getCellGrid() {
        return cellGrid;
    }

    /**
     * Gets the entity grid
     * @return the entity grid for the level Object
     */
    public Entity[][] getEntityGrid() {
        return entityGrid;
    }

    /**
     * Gets the level name
     * @return the name of the Level, for saving
     */
    String getLevelName() {
        return this.levelName;
    }

    /**
     * Gets the list of Enemies that need to move each tick
     * @return the array of Enemies
     */
    private ArrayList<Enemy> getEnemies() {
        return this.enemies;
    }

    /**
     * Sets the cell grid
     * @param cellGrid the cell grid for the level Object
     */
    public void setCellGrid(Cell[][] cellGrid) {
        Level.cellGrid = cellGrid;
    }

    /**
     * Sets the entity grid
     * @param entityGrid the entity grid for the level Object
     */
    public void setEntityGrid(Entity[][] entityGrid) {
        Level.entityGrid = entityGrid;
    }

    /**
     * Builds the Level
     * @param level the Level file to build
     * @throws FileNotFoundException if the file doesn't exist
     */
    private void buildLevel(String level) throws FileNotFoundException {

        Scanner reader;
        if(level.contains("SAVE")) {
            reader = new Scanner(new File("Users/" + GUI.USER_NAME + "/" + level + ".txt"));
        } else {
            reader = new Scanner(new File("Level_Files/" + level + ".txt"));
        }
        reader.useDelimiter(",");

        buildBasicGrids(reader);

    }

    /**
     * Builds basic grids
     * @param reader not-a-file-reader
     */
    private void buildBasicGrids(Scanner reader) {

        int x = reader.nextInt();
        int y = reader.nextInt();

        // Throw away the rest of the line
        reader.nextLine();

        cellGrid = new Cell[x][y];
        entityGrid = new Entity[x][y];

        for (int i = 0 ; i < y ; i++) {

            char[] row = reader.nextLine().toCharArray();

            for (int j = 0 ; j < x ; j++) {

                if ('#' == row[j]) {
                    // This is a wall
                    cellGrid[j][i] = new Wall();
                } else {
                    // This is a ground
                    cellGrid[j][i] = new Ground();
                }

            }
        }

        buildGrids(readRemainingLines(reader));

    }

    /**
     * Reads in the remaining lines in the file, to be built elsewhere
     * @param reader not-a-file-reader
     * @return The String containing the Entitys
     */
    private String readRemainingLines(Scanner reader) {

        StringBuilder stringBuilder = new StringBuilder();

        while (reader.hasNextLine()) {
            stringBuilder.append(reader.nextLine());
        }

        reader.close();

        return stringBuilder.toString().toUpperCase();

    }

    /**
     * Finishes the build process
     * @param file the remaining data to build
     */
    private void buildGrids(String file) {

        StringTokenizer t = new StringTokenizer(file, ",");

        String label;
        Position p = new Position(0, 0);
        int dr;

        while (t.hasMoreTokens()) {

            label = t.nextToken();

            if (!"INVENTORY".equals(label)) {
                p = new Position(parseInt(t.nextToken()), parseInt(t.nextToken()));
            }

            if ("PLAYER".equals(label) || label.contains("ENEMY")) {

                dr = parseInt(t.nextToken());

                if ("PLAYER".equals(label)) {
                    entityGrid[p.x][p.y] = new Player(p, dr);
                } else if (label.contains("ENEMY")) {
                    addEnemy(label, p, dr, t);
                }

            } else if ("KEYDOOR".equals(label)) {

                String colour = t.nextToken();
                cellGrid[p.x][p.y] = new KeyDoor(colour);
                p = new Position(parseInt(t.nextToken()), parseInt(t.nextToken()));
                entityGrid[p.x][p.y] = new Key(colour);

            } else if ("TOKEN".equals(label)) {
                entityGrid[p.x][p.y] = new Token();
            } else if ("TOKENDOOR".equals(label)) {
                dr = parseInt(t.nextToken());
                cellGrid[p.x][p.y] = new TokenDoor(dr);
            } else if ("GOAL".equals(label)) {
                cellGrid[p.x][p.y] = new Goal();
            } else if ("FIRE".equals(label)) {
                cellGrid[p.x][p.y] = new Fire();
            } else if ("WATER".equals(label)) {
                cellGrid[p.x][p.y] = new Water();
            } else if ("FIREBOOTS".equals(label)) {
                entityGrid[p.x][p.y] = new FireBoots();
            } else if ("FLIPPERS".equals(label)) {
                entityGrid[p.x][p.y] = new Flippers();
            } else if ("TELEPORTER".equals(label)) {

                Teleporter temp = new Teleporter();

                cellGrid[p.x][p.y] = temp;

                p = new Position(parseInt(t.nextToken()), parseInt(t.nextToken()));

                cellGrid[p.x][p.y] = new Teleporter(temp);
            } else if ("INVENTORY".equals(label)) {
                readInventory(t);
            }

        }

    }

    /**
     * Used to add enemies to the grid and their own array
     * @param label the name of the enemy
     * @param p their position
     * @param dr their direction
     * @param tokenizer the string of tokens to read from
     */
    private void addEnemy(String label, Position p, int dr, StringTokenizer tokenizer) {

        if ("SMARTENEMY".equals(label)) {
            entityGrid[p.x][p.y] = new SmartEnemy(p, dr);
        } else if ("DUMBENEMY".equals(label)) {
            entityGrid[p.x][p.y] = new DumbEnemy(p, dr);
        } else if ("WALLENEMY".equals(label)) {
            entityGrid[p.x][p.y] = new WallEnemy(p, dr);
        } else if ("LINEENEMY".equals(label)) {
            entityGrid[p.x][p.y] = new LineEnemy(p, dr);
        }

        this.enemies.add((Enemy) entityGrid[p.x][p.y]);

    }

    /**
     * Used to move Enemies within the level
     * @param level this thing, soon (tm)
     * @param grid the grid in which they move, not yet updates
     * @return the new grid
     */
    Entity[][] moveEnemys(Level level, Entity[][] grid) {

        ArrayList<Enemy> enemies = level.getEnemies();
        Player player = level.getPlayer();

        // Move all the enemies after the player has moved.
        for (Enemy e : enemies) {

            if (level.getPlayer() != null && player.getStatus()) {
                // Player exists and isn't dead
                e.updateGrids(level.getCellGrid(), grid);
            }

            grid = e.move(level, grid);

        }

        return grid;
    }

    /**
     * Used to build the player inventory
     * @param tokenizer the things that does the reading
     */
    private void readInventory(StringTokenizer tokenizer) {

        Player player = this.getPlayer();

        if(tokenizer.hasMoreTokens()==true) {

            String item = tokenizer.nextToken();

            if ("FLIPPERS".equals(item)) {
                player.addItem(this, new Flippers());
            } else if ("FIREBOOTS".equals(item)) {
                player.addItem(this, new FireBoots());
            } else if ("KEY".equals(item)) {
                player.addItem(this, new Key(tokenizer.nextToken()));
            } else if ("TOKEN".equals(item)) {
                player.addItem(this, new Token());
                player.setTokenCount(Integer.parseInt(tokenizer.nextToken()));
            }
        }

    }

    /**
     * Gets the Player object from the level
     * @return the Player object
     */
    Player getPlayer() {

        for (Entity[] row : entityGrid) {
            for (Entity entity : row) {
                if (entity instanceof Player) {
                    return (Player) entity;
                }
            }
        }

        return null;

    }

    /**
     * Used to find an Item or Cell in the Level grids
     * @param grid which grid to search
     * @param thing what you want to find
     * @param <T> Cell or Entity
     * @return The location if not null
     */
    <T> int[] getLocation(T[][] grid, T thing) {

        for (int x = 0 ; x < entityGrid.length ; x++ ) {
            for (int y = 0 ; y < entityGrid[x].length ; y++ ) {

                if (thing == grid[x][y]) {
                    return new int[] {x, y};
                }

            }
        }

        return null;
    }

}
