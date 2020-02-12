package Challenge;

import java.util.ArrayList;

/**
 * Class used to represent a User profile
 * @author Samuel Roach, Blake Davies
 * @version 1.0
 */
class User {

    /**
     * The username for the profile
     */
    private String userName;

    /**
     * The list of the players scores for each level
     */
    private ArrayList<Integer> scores = new ArrayList<>();

    /**
     * Constructor for User class
     * @param userName The username of the created User
     */
    User(String userName) {
        this.userName = userName;
    }

    /**
     * Getter to retrieve the Username of this class
     * @return The username
     */
    String getUserName() {
        return this.userName;
    }

    /**
     * Getter to retrieve the scores for the user
     * @return The array list of scores
     */
    ArrayList<Integer> getScores() {
        return scores;
    }

    /**
     * Setter to set the level scores for the User
     * @param scores the users scores
     */
    void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    /**
     * Method to add a score for a level to a User
     * @param level The level for which the score is for
     * @param score The score which was achieved on the level
     */
    void addScore(Level level, int score){

        int levelNo = Integer.parseInt(level.getLevelName().substring(6));

        //Lumberjack.log(2,"USER " + score);

        if ((getScores().get(levelNo-1) > score) || (0 == getScores().get(levelNo-1))) {
            scores.set((levelNo - 1), score);
        }

    }

}
