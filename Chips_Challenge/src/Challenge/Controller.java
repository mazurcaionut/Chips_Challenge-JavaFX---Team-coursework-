package Challenge;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

/**
 * This class is designed to be used to allows the player to be controlled
 * by the user. It allows the input of arrow keys in order to move the player.
 * @author George Carpenterm, Ioan Mazurca
 * @version 1.0
 */
class Controller {

    /**
     * Tracks inventory change
     */
    private static boolean changeInventory = true;

    private static boolean changePauseMenu = true;

    /**
     * Saves the players scores after a level is completed
     */
    private final Save save = new Save();

    /**
     * The list of users who have played a level
     */
    private static ArrayList<String> leaderboardUsers;

    private static ArrayList<Integer> leaderboardScores;


    /**
     * Takes in certain inputs and outputs player actions.
     * @param event The event to be read.
     * @param level The level being played
     * @param game The game to be altered and re-rendered.
     * @param canvas The canvas for rendering the game.
     * @param panes The panes that can be changed between as a result
     */
    void processKeyEvent(KeyEvent event, Level level, Game game, Canvas canvas, BorderPane[] panes) {

        // Get the event code
        KeyCode key = event.getCode();

        if (key.isArrowKey()) {

            // Grab the player, current entity grid and the enemy array
            Entity[][] newGrid;
            Player player = level.getPlayer();

            ArrayList<KeyCode> codes = new ArrayList<>(Arrays.asList(
                    KeyCode.UP, KeyCode.RIGHT, KeyCode.DOWN, KeyCode.LEFT
            ));

            newGrid = player.move(codes.indexOf(key), level);
            newGrid = level.moveEnemys(level, newGrid);

            // Update the Level object for drawing
            level.setEntityGrid(newGrid);

            if (player.getGameStatus()) {
                player.setGameStatus();

                GUI.END_TIME = System.nanoTime();
                GUI.ELAPSED_TIME = GUI.END_TIME - GUI.START_TIME;
                GUI.CONVERTED_TIME = TimeUnit.SECONDS.convert(GUI.ELAPSED_TIME, TimeUnit.NANOSECONDS);
                GUI.scene.setRoot(panes[0]);
                Main.window.setScene(GUI.scene);

                System.out.println(GUI.CONVERTED_TIME);

                game.getUser().addScore(level, (int) GUI.CONVERTED_TIME);
                this.save.saveProfile(game.getUser());

                update(level);
            }

            if (level.getPlayer() != null && player.getStatus()) {
                // Player should be alive
                game.drawGame(level, canvas);
            } else {
                GUI.LEVEL = new Level(level.getLevelName());
                GUI.scene.setRoot(panes[1]);
                Main.window.setScene(GUI.scene);
            }

        }

        event.consume();
    }

    /**
     * Handles Menu events
     * @param event the events to respond to
     * @param root the stackPane to replace / remove
     */
    void processMenuEvent(KeyEvent event, StackPane root, Level level) {

        if (KeyCode.ESCAPE == event.getCode()) {
            if(changePauseMenu) {
                root.lookup("#pauseMenu").toFront();
                changePauseMenu = false;
            } else {
                root.lookup("#maps").toFront();
                changePauseMenu = true;
            }

        } else if (KeyCode.E == event.getCode()) {

            if (changeInventory) {
                root.lookup("#Inventory").toBack();
                root.getChildren().set(0, GUI.inventory(level));
                root.lookup("#Inventory").toFront();
                changeInventory = false;
            } else {
                root.lookup("#maps").toFront();
                changeInventory = true;
            }

        }
    }

    /**
     * The scores for that level, ordered
     * @param level the level to collect scores for
     */
    private static void calculateLeaderboardScores (Level level) {

        ArrayList<Integer> userScores = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();

        File path = new File("Users/");

        File[] files = path.listFiles();
        assert files != null;

        for (File file : files) {
            File scoresFile = new File(file + "/scores.txt");

            userNames.add(file.getPath().substring(6));
            userScores.add(returnLevelScore(level, scoresFile));
        }

        leaderboardUsers = userNames;
        leaderboardScores = userScores;
    }

    /**
     * Gets level scores
     * @param level the level to fetch scores for
     * @param file the file to get them from
     * @return the scores for that level
     */
    private static int returnLevelScore (Level level, File file) {

        int levelNo = parseInt(level.getLevelName().substring(6));

        try {

            Scanner fileRead = new Scanner(file);

            IntStream.range(0, (levelNo - 1)).forEach(i ->
                    fileRead.nextLine());

            return parseInt(fileRead.nextLine().split(" - ")[1]);

        } catch (Exception e) {

            //Lumberjack.log(1, "return level score" + e);
            return 0;
        }
    }

    /**
     * My Bubbles!
     * the array of scores to sort
     */
    private void bubbleSort() {

        int n = leaderboardScores.size();
        int intChange;
        String stringChange;

        for (int i = 0 ; i < n - 1 ; i ++ ) {
            for (int j = 0 ; j < n - i - 1 ; j ++ ) {

                if (leaderboardScores.get(j) > leaderboardScores.get(j + 1)) {
                    // swap arr[j+1] and arr[i]
                    intChange = leaderboardScores.get(j);
                    leaderboardScores.set(j, leaderboardScores.get(j + 1));
                    leaderboardScores.set(j + 1, intChange);

                    stringChange = leaderboardUsers.get(j);
                    leaderboardUsers.set(j, leaderboardUsers.get(j + 1));
                    leaderboardUsers.set(j + 1, stringChange);
                }

            }
        }
    }

    /**
     * Processes the Mini map
     * @param event What makes it appear
     * @param level the level to render
     * @param mini the mini map object
     * @param miniMapCanvas the canvas to render it to
     */
    void processMiniMap(KeyEvent event, Level level, MiniMap mini, Canvas miniMapCanvas) {
        mini.drawMap(level, miniMapCanvas);
        event.consume();
    }

    /**
     * Method to help reset the level and make a new level
     * @param levelName The level's name.
     * @return A new level with the inputted name
     */
    Level makeLevel(String levelName) {
        return new Level(levelName);
    }

    /**
     * Used to update High Scores
     * @param level the Level to update them for
     */
    void update(Level level) {
        calculateLeaderboardScores(level);
        bubbleSort();
    }

    /**
     * Gets the User names for the High Scores
     * @return the array of names
     */
    ArrayList<String> getLeaderboardUsers() {
        return leaderboardUsers;
    }

    /**
     * Gets the User Scores for the High Scores
     * @return the array of scores
     */
    ArrayList<Integer> getLeaderboardScores() {
        return leaderboardScores;
    }
}