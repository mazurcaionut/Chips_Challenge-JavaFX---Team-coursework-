package Challenge;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import javafx.util.Duration;

import java.io.File;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The GUI Class is entirely self explanatory
 */
class GUI {

    /**
     * Window width
     */
    private static final int WINDOW_WIDTH = 960;

    /**
     * Window height
     */
    private static final int WINDOW_HEIGHT = 720;

    /**
     * Canvas width
     */
    private static final int CANVAS_WIDTH = 960;

    /**
     * Canvas height
     */
    private static final int CANVAS_HEIGHT = 684;

    /**
     * The canvas used for the game
     */
    private static Canvas gameCanvas;

    /**
     * The canvas used for the mini map
     */
    private static Canvas miniMapCanvas;

    private static HBox menuInventory;

    private static AnchorPane displayInventory;


    /**
     * The StackPane used to make the transition between the menu, inventory and the game
     */
    private static StackPane stack;

    /**
     * The game object
     */
    private static Game game;

    /**
     * The mini map object
     */
    private static final MiniMap miniMap = new MiniMap();

    /**
     * Controller for the entire program
     */
    private static final Controller controller = new Controller();

    /**
     * The current user name
     */
    public static String USER_NAME;

    /**
     * The stylesheet for the entire program
     */
    private static String STYLESHEET = "layout.css";

    /**
     * The level object
     */
    static Level LEVEL;

    /**
     * The start time for a level
     */
    static long START_TIME = 0;

    /**
     * The finish time for a level
     */
    static long END_TIME;

    /**
     * The elapsed time for a level
     */
    static long ELAPSED_TIME;

    /**
     * The time used in the leaderboards
     */
    static long CONVERTED_TIME;

    /**
     * The start method, this will initialise the program
     */
    public static final Scene scene = new Scene(begin(), WINDOW_WIDTH, WINDOW_HEIGHT);

    /**
     * Used to display the message of the day, at all times
     * @return the message label
     */
    private static Label messageOfTheDay() {

        Label message = new Label();
        AtomicReference<String> stuff = new AtomicReference<>(new Ping().getPing());

        message.textProperty().set(stuff.get());
        message.setTextFill(Color.rgb(200, 200, 200));

        Timeline timeline = new Timeline(
                new KeyFrame(new Duration(30000), e -> {
                    stuff.set(new Ping().getPing());
                    message.textProperty().set(stuff.get());
                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return message;
    }

    /**
     * Used to build the lower bar for the GUI
     * @return the lower bar
     */
    private static HBox bottomBar(){

        HBox bottomBar = new HBox();

        bottomBar.setPrefHeight(36);
        bottomBar.setPadding(new Insets(10, 10, 10, 10));
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.getChildren().add(messageOfTheDay());
        bottomBar.setStyle("-fx-background-color: #222222");
        bottomBar.setMinHeight(36);

        return bottomBar;
    }

    /**
     * Used to display the start screen
     * @return the start screen
     */
    private static BorderPane begin() {
        BorderPane root = new BorderPane();

        Button startButton = new Button("START");
        Button leaderboard = new Button("LEADERBOARD");
        Button quit = new Button("Quit");

        VBox vBox = new VBox();
        vBox.getChildren().addAll(startButton, leaderboard, quit);

        root.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);

        root.setBottom(bottomBar());
        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        startButton.setOnAction(e -> {
            scene.setRoot(userSelection());
            Main.window.setScene(scene);
        });

        leaderboard.setOnAction(e -> {
            scene.setRoot(levelLeaderboard());
            Main.window.setScene(scene);
        });

        quit.setOnAction(e -> System.exit(0));

        return root;
    }

    /**
     * Used to make buttons for the GUI
     * @param files the files to make buttons for
     * @param menu the Menu to display them on
     * @return the ArrayList of button objects
     */
    private static ArrayList<Button> makeLevelButtons(File[] files, VBox finishedLevels, VBox incompletedLevels) {

        ArrayList<Button> buttons = new ArrayList<>();
        String remove;
        // DO NOT ADD A NEW LEVEL WHOSE NUMBER IS NOT CONSECUTIVE I.E. IF THERE ARE 20 LEVELS DO NOT ADD A LEVEL
        // CALLED LEVEL_24. CALL IT LEVEL 21. DUE TO BUCKET SORT.
        for(int i = 0 ; i < files.length ; i++ ) {
            buttons.add(null);
        }
        for (File file : files) {
            remove = file.getName();
            remove = remove.substring(0, remove.length() - 4);
            int index = Integer.parseInt(remove.substring(6));
            buttons.set(index - 1, new Button(remove));

        }
        ArrayList<Integer> scores = Save.loadPlayerScores(USER_NAME);
        boolean ok = true;
        for (int i = 0 ; i < files.length; i++) {
            buttons.get(i).getStyleClass().add("button1");
            if(scores.get(i)!=0) {
                finishedLevels.getChildren().add(buttons.get(i));
            } else {
                if(ok) {
                    incompletedLevels.getChildren().add(buttons.get(i));
                    ok=false;
                }
                else {
                    buttons.get(i).setDisable(true);
                    incompletedLevels.getChildren().add(buttons.get(i));
                }
            }
        }

        finishedLevels.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        incompletedLevels.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        return buttons;

    }

    private static ArrayList<Button> makeLeaderboardButtons(File[] files, VBox vBox) {
        ArrayList<Button> buttons = new ArrayList<>();
        String remove;
        // DO NOT ADD A NEW LEVEL WHOSE NUMBER IS NOT CONSECUTIVE I.E. IF THERE ARE 20 LEVELS DO NOT ADD A LEVEL
        // CALLED LEVEL_24. CALL IT LEVEL 21. DUE TO BUCKET SORT.
        for(int i = 0 ; i < files.length ; i++ ) {
            buttons.add(null);
        }
        for (File file : files) {
            remove = file.getName();
            remove = remove.substring(0, remove.length() - 4);
            int index = Integer.parseInt(remove.substring(6));
            buttons.set(index - 1, new Button(remove));

        }

        for (int i = 0 ; i < files.length; i++) {
            buttons.get(i).getStyleClass().add("button1");
            vBox.getChildren().add(buttons.get(i));
        }

        vBox.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        return buttons;
    }

    private static ArrayList<Button> makeSaveButtons(File[] files, VBox vBox) {
        ArrayList<Button> buttons = new ArrayList<>();
        String remove;

        for (File file : files) {
            remove = file.getName();
            remove = remove.substring(0, remove.length() - 4);
            buttons.add(new Button(remove));
        }

        for (int i = 0 ; i < files.length-1; i++) {
            buttons.get(i).getStyleClass().add("button1");
            vBox.getChildren().add(buttons.get(i));
        }

        vBox.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        return buttons;
    }

    /**
     * Used to display the a Level Leaderboard
     * @return the Leaderboard display for that Level
     */
    private static BorderPane levelLeaderboard() {

        BorderPane root = new BorderPane();
        VBox menu = new VBox();

        File path = new File("Level_Files/");

        File[] files = path.listFiles();

        ArrayList<Button> buttons = makeLeaderboardButtons(Objects.requireNonNull(files), menu);

        root.setBottom(bottomBar());
        menu.setAlignment(Pos.CENTER);
        root.setCenter(menu);
        root.setMargin(menu, new Insets(100,0,0,0));


        for (Button button : buttons) {
            button.setOnAction(e -> {
                String levelName = button.getText();
                scene.setRoot(leaderboard(levelName));
                Main.window.setScene(scene);
            });
        }

        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    /**
     * Used to display the Main Leaderboard for all Levels
     * @param name The name of the Levels to atribute
     * @return the leaderboard display
     */
    private static BorderPane leaderboard(String name) {

        BorderPane root = new BorderPane();
        VBox vBox = new VBox();
        Label title;
        String user;

        Button back = new Button("Back");

        AnchorPane.setRightAnchor(back, 20.0);
        AnchorPane.setBottomAnchor(back,20.0);
        AnchorPane bottomRight = new AnchorPane();
        bottomRight.getChildren().add(back);

        LEVEL = controller.makeLevel(name);
        controller.update(LEVEL);

        boolean ok = true;
        int l = 0;

        {
            int i = 0 ;
            while (i < controller.getLeaderboardScores().size() && ok) {
                if (controller.getLeaderboardScores().get(i) != 0) {

                    user = (l + 1) + ")" + controller.getLeaderboardUsers().get(i) +
                            " - " + controller.getLeaderboardScores().get(i);
                    title = new Label(user);
                    style(title);
                    vBox.getChildren().add(title);

                    l++;
                }

                if(l == 3) {
                    ok = false;
                }
                i++;
            }
        }

        if (ok) {

            for (int i = l ; i < 3 ; i ++ ) {
                user = (l + 1) + ")" + controller.getLeaderboardUsers().get(0) + " - 0";
                title = new Label(user);
                style(title);
                vBox.getChildren().add(title);

                l++;
            }

        }

        root.setBottom(bottomBar());
        vBox.setAlignment(Pos.CENTER_LEFT);
        root.setCenter(vBox);
        root.setRight(bottomRight);

        back.setOnAction(e -> {
            scene.setRoot(begin());
            Main.window.setScene(scene);
        });

        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        return root;
    }

    /**
     * Used to display the user selection GUI
     * @return the User selection screen
     */
    private static BorderPane userSelection() {

        BorderPane root = new BorderPane();

        VBox menu = new VBox();

        EditableButton newUser = new EditableButton("Create user profile");

        ComboBox<String> loadUser = new ComboBox<>();

        File path = new File("Users/");

        File[] files = path.listFiles();

        assert files != null;
        for (File file : files) {
            loadUser.getItems().add(file.getName());
        }

        loadUser.setPromptText("Select user profile");

        loadUser.setOnAction(e -> {
            USER_NAME = loadUser.getSelectionModel().getSelectedItem();
            scene.setRoot(loadGame());
            Main.window.setScene(scene);
        });

        Button back = new Button("Back");

        back.setOnAction(e -> {
            scene.setRoot(begin());
            Main.window.setScene(scene);
        });

        Button quit = new Button("Quit");

        menu.getChildren().addAll(newUser, loadUser, back, quit);
        menu.setAlignment(Pos.CENTER);

        root.setCenter(menu);
        root.setBottom(bottomBar());
        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        quit.setOnAction(e -> {
                System.out.println("Adios Amigo!");
                System.exit(0);
        });

        return root;
    }

    /**
     * Used to start a new game
     * @return the new Game GUI
     */
    private static BorderPane newGame() {
        BorderPane root = new BorderPane();

        VBox vBox = new VBox();

        Button startButton = new Button("New Game");

        vBox.getChildren().add(startButton);
        vBox.setAlignment(Pos.CENTER);

        File path = new File("Level_Files/");
        File[] files = path.listFiles();

        assert files != null;

        startButton.setOnAction(e -> {
            String levelName = files[0].getName();
            levelName = levelName.substring(0, levelName.length()-4);
            scene.setRoot(gaming(levelName));
            Main.window.setScene(scene);
        });

        root.setCenter(vBox);
        root.setBottom(bottomBar());

        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    /**
     * Used to load a game from a file
     * @return the level to display
     */
    private static BorderPane loadGame() {
        BorderPane root = new BorderPane();

        VBox vBox = new VBox();

        Button startButton = new Button("New game");
        Button loadButton = new Button("Load game");

        File path = new File("Level_files/");
        File[] files = path.listFiles();

        assert files != null;

        startButton.setOnAction(e -> {
            String levelName = files[0].getName();
            levelName = levelName.substring(0,levelName.length() - 4);
            scene.setRoot(gaming(levelName));
            Main.window.setScene(scene);
        });

        vBox.getChildren().addAll(startButton,loadButton);
        vBox.setAlignment(Pos.CENTER);

        root.setCenter(vBox);
        root.setBottom(bottomBar());

        loadButton.setOnAction(e -> {
            scene.setRoot(displayLevel());
            Main.window.setScene(scene);
        });

        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    /**
     * Used to display a level to the GUI
     * @return the level, displayed
     */
    private static BorderPane displayLevel() {

        BorderPane root = new BorderPane();

        HBox container = new HBox();

        VBox finishedLevels = new VBox();
        VBox savedLevels = new VBox();
        VBox incompletedLevels = new VBox();

        Label levels = new Label("COMPLETED LEVELS");
        style(levels);

        Label saved = new Label("SAVED LEVELS");
        style(saved);

        Label next = new Label("NEXT LEVELS");
        style(next);


        finishedLevels.getChildren().add(levels);
        savedLevels.getChildren().add(saved);
        incompletedLevels.getChildren().add(next);


        File path = new File("Level_Files/");
        File savePath = new File("Users/" + USER_NAME + "/");

        File[] saveFiles = savePath.listFiles();

        ArrayList<Button> buttons1 = makeSaveButtons(Objects.requireNonNull(saveFiles), savedLevels);

        File[] files = path.listFiles();

        ArrayList<Button> buttons = makeLevelButtons(Objects.requireNonNull(files), finishedLevels, incompletedLevels);


        finishedLevels.setMargin(finishedLevels.getChildren().get(0), new Insets(0,0,0,-40));
        incompletedLevels.setMargin(incompletedLevels.getChildren().get(0), new Insets(0,0,0,10));

        root.setBottom(bottomBar());

        container.getChildren().addAll(finishedLevels, savedLevels, incompletedLevels);
        container.setMargin(container.getChildren().get(0), new Insets(0,0,0,70));
        container.setMargin(container.getChildren().get(1), new Insets(0,0,0,60));
        container.setMargin(container.getChildren().get(2), new Insets(0,0,0,120));

        root.setCenter(container);
        root.setMargin(container, new Insets(150,0,0,0));

        for (Button button : buttons1) {
            button.setOnAction(e -> {
                String levelName = button.getText();
                scene.setRoot(gaming(levelName));
                Main.window.setScene(scene);
            });
        }

        for (Button button : buttons) {
            button.setOnAction(e -> {
                String levelName = button.getText();
                scene.setRoot(gaming(levelName));
                Main.window.setScene(scene);
            });
        }


        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    public static void updateInventory(Level level) {
        ArrayList<Item> playerInventory = level.getPlayer().getInventory();

        menuInventory = new HBox();


        for (Item item : playerInventory) {
            menuInventory.getChildren().add(new ImageView(SpriteConverter.resize(item.getSprite(), 50, 50)));
            menuInventory.getChildren().add(new Separator(Orientation.VERTICAL));
        }

        menuInventory.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());
    }

    /**
     * Used to display the Players inventory
     * @param level the current level
     * @return the displayed inventory
     */
    public static AnchorPane inventory(Level level) {
        displayInventory = new AnchorPane();

        updateInventory(level);

        AnchorPane.setTopAnchor(menuInventory, 600.0);
        AnchorPane.setLeftAnchor(menuInventory, 200.0);
        AnchorPane.setRightAnchor(menuInventory, 200.0);
        AnchorPane.setBottomAnchor(menuInventory,20.0);

        menuInventory.setId("miniInventory");

        menuInventory.setAlignment(Pos.CENTER_LEFT);

        displayInventory.getChildren().add(menuInventory);
        displayInventory.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        displayInventory.setId("Inventory");

        return displayInventory;
    }

    /**
     * Used to display the Pause Menu
     * @return the Pause menu
     */
    private static AnchorPane pauseMenu(){
        AnchorPane pause = new AnchorPane();
        VBox vBox = new VBox();
        vBox.setPrefSize(200,200);


        Button goBack = new Button("Return to main menu");
        Button exitGame = new Button("Exit");

        vBox.getChildren().addAll(goBack, exitGame);
        vBox.setMargin(vBox.getChildren().get(0), new Insets(50,0,0,0));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);

        goBack.setOnAction(e -> {
            scene.setRoot(userSelection());
            Main.window.setScene(scene);
        });

        exitGame.setOnAction(e -> System.exit(0));

        VBox.setMargin(exitGame, new Insets(0,0,20,0));

        AnchorPane.setBottomAnchor(vBox,180.0);
        AnchorPane.setLeftAnchor(vBox,250.0);
        AnchorPane.setTopAnchor(vBox,150.0);
        AnchorPane.setRightAnchor(vBox,250.0);

        pause.getChildren().add(vBox);
        pause.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        pause.setId("pauseMenu");

        return pause;
    }

    /**
     * Used to display the Level Finished Menu
     * @return the Level finished Menu
     */
    private static BorderPane gameSucceed() {
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();

        Label title = new Label("LEVEL FINISHED SUCCESSFULLY");
        style(title);

        Button selectLevel = new Button("SELECT LEVELS");
        Button returnMenu = new Button("USER SELECTION");
        Button quit = new Button("Quit");

        vBox.getChildren().addAll(title, selectLevel, returnMenu, quit);
        vBox.setAlignment(Pos.CENTER);

        selectLevel.setOnAction(e -> {
            scene.setRoot(displayLevel());
            Main.window.setScene(scene);
        });

        returnMenu.setOnAction(e -> {
            scene.setRoot(userSelection());
            Main.window.setScene(scene);
        });

        quit.setOnAction(e -> System.exit(0));

        root.setCenter(vBox);
        root.setBottom(bottomBar());
        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    /**
     * Used to display the Game Over Menu
     * @return the "you ded" Menu
     */
    private static BorderPane gameOver() {
        BorderPane root = new BorderPane();
        VBox vBox = new VBox();

        Label title = new Label("GAME OVER");
        style(title);

        Button restartLevel = new Button("RESTART LEVEL");
        Button returnMenu = new Button("USER SELECTION");
        Button quit = new Button("Quit");

        restartLevel.setOnAction(e -> {
            scene.setRoot(gaming(LEVEL.getLevelName()));
            Main.window.setScene(scene);
        });

        returnMenu.setOnAction(e -> {
            scene.setRoot(userSelection());
            Main.window.setScene(scene);
        });

        quit.setOnAction(e -> System.exit(0));

        vBox.getChildren().addAll(title, restartLevel, returnMenu, quit);
        vBox.setAlignment(Pos.CENTER);

        root.setCenter(vBox);
        root.setBottom(bottomBar());
        root.getStylesheets().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        return root;
    }

    /**
     * Used to display the title screen
     * @param title the title screen GUI
     */
    private static void style(Label title) {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        title.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC,30));
        title.setEffect(dropShadow);
    }

    /**
     * Used to display the game
     * @param name the name of the level to display
     * @return the displayed game
     */
    private static BorderPane gaming(String name) {

        // jack.log(2, "user created " + userName);
        game = new Game(USER_NAME);

        BorderPane root = new BorderPane();
        root.setPrefSize(960,670);

        BorderPane drawing = new BorderPane();
        drawing.setPrefSize(960,670);

        stack = new StackPane();
        StackPane maps = new StackPane();

        gameCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        AnchorPane awesome = new AnchorPane();
        BorderPane mini = new BorderPane();
        mini.setPrefSize(150,150);
        miniMapCanvas = new Canvas(150, 150);

        drawing.setCenter(gameCanvas);
        drawing.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());
        drawing.setId("game");

        LEVEL = controller.makeLevel(name);

        game.drawGame(LEVEL, gameCanvas);

        mini.setCenter(miniMapCanvas);
        mini.setStyle("-fx-border-color: #42832d ; -fx-border-width: 2px ");
        miniMap.drawMap(LEVEL, miniMapCanvas);

        AnchorPane.setBottomAnchor(mini, 500.0);
        AnchorPane.setLeftAnchor(mini, 750.0);

        awesome.getChildren().add(mini);
        awesome.getStyleClass().add(GUI.class.getResource(STYLESHEET).toExternalForm());

        maps.getChildren().add(drawing);
        maps.getChildren().add(awesome);

        maps.setId("maps");

        stack.getChildren().add(inventory(LEVEL));
        stack.getChildren().add(pauseMenu());
        stack.getChildren().add(maps);
        root.setBottom(bottomBar());
        root.setCenter(stack);

        START_TIME = System.nanoTime();

        root.addEventFilter(KeyEvent.KEY_PRESSED, event ->
                controller.processKeyEvent(event, LEVEL, game, gameCanvas, new BorderPane[] {gameSucceed(), gameOver()}));
        root.addEventFilter(KeyEvent.KEY_PRESSED, event ->
                controller.processMiniMap(event, LEVEL, miniMap, miniMapCanvas));
        root.addEventFilter(KeyEvent.KEY_PRESSED, event ->
                controller.processMenuEvent(event, stack, LEVEL));


        System.out.println(drawing.getId());

        return root;

    }

    /**
     * A class used to extend Button which allows them to be edited
     * @author Ioan Mazurca
     * @version 2.0
     */
    static class EditableButton extends Button {

        String user;

        final TextField tf = new TextField();

        EditableButton(String text) {

            setText(text);

            setOnMouseClicked(e -> {
                //tf.setText(getText());
                setText("");
                setGraphic(tf);
            });

            tf.setOnAction(ae -> {

                //File path = new File("Users/" + tf.getText());
                //path.mkdir();

                USER_NAME = tf.getText();

                scene.setRoot(newGame());
                Main.window.setScene(scene);

            });
        }


        public String getName() {
            return this.user;
        }

    }

}
