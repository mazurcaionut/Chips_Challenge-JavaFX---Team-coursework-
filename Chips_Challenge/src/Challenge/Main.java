package Challenge;

import javafx.application.Application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.stage.Stage;

import java.nio.file.Paths;

/**
 * This done mainy stuff
 * @author Everyone basically
 * @version 9001
 */
public class Main extends Application {

    /**
     * Plays music!
     */
    private static MediaPlayer MEDIA_PLAYER;

    /**
     * Winedos 9 when?
     */
    static Stage window;

    /**
     * Mainy!
     * @param args Stuff parsed in
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the thing!
     * @param primaryStage the stage we get for free!
     */
    public void start(Stage primaryStage) {
        window = primaryStage;

        window.setTitle("Jungle Hunt");
        window.setScene(GUI.scene);
        window.show();

        try {
            //Media media = new Media(Paths.get("D:\\IdeaProjects\\CS-230\\Chips_Challenge\\music\\music.mp3").toUri().toString());
            Media media = new Media(Paths.get("music/music.mp3").toUri().toString());
            MEDIA_PLAYER = new MediaPlayer(media);
            MEDIA_PLAYER.setCycleCount(MediaPlayer.INDEFINITE);
            MEDIA_PLAYER.setVolume(0.2);
            MEDIA_PLAYER.play();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

}
