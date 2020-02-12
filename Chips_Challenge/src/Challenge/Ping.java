package Challenge;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.IntStream;

/**
 * Class used to Ping a web server and return a Message Of The Day to be
 * displayed at all times during the game play
 * @author George Carpenter
 * @version 2.0
 */
class Ping {

    /**
     * Ping!
     * @return The returned data, in String form
     */
    String getPing() {

        try {
            return pingAPI();
        } catch (Exception e) {
            // e.printStackTrace();
            return "Oops, Liam killed the API!";
            // Assuming Liam doesn't kill the API this won't happen :p
        }

    }

//
//                    ,        ,
//               /(        )`
//               \ \___   / |
//               /- _  `-/  '
//              (/\/ \ \   /\
//              / /   | `    \
//              O O   ) /    |
//              `-^--'`<     '
//             (_.)  _  )   /
//              `.___/`    /
//                `-----' /
//   <----.     __ / __   \
//   <----|====O)))==) \) /====
//   <----'    `--' `.__,' \
//                |        |
//                 \       /
//            ______( (_  / \______
//          ,'  ,-----'   |        \
//          `--{__________)        \/
//
//  Figure II - Beastie
//

    /**
     * Calculates data to send to the web server
     * @return the data to ping towards the web server
     * @throws Exception if the data is incorrect
     */
    private String pingAPI() throws Exception {

        String puzzleURL = "http://cswebcat.swan.ac.uk/puzzle";
        String inputString = makeConnection(puzzleURL);
        char[] inputArray = inputString.toCharArray();

        StringBuilder out = new StringBuilder();

        IntStream.range(0, inputArray.length).forEach(i -> {
            char c = 0 == i % 2 ?
                shiftUp(inputArray[i]) :
                shiftDown(inputArray[i]);
            out.append(c);
        });

        String messageURL = "http://cswebcat.swan.ac.uk/message?solution=";
        return makeConnection(messageURL + out);

    }

    /**
     * Makes a connection to the web server
     * @param URL the URL to connect to
     * @return the data returned from the web server
     * @throws Exception If the web server is not connectable to
     */
    private String makeConnection(String URL) throws Exception {

        java.net.URL url = new URL(URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        return br.readLine();
    }

    /**
     * Shifts Characters up, A to B etc
     * @param c the character to shift
     * @return the shifted character
     */
    private char shiftUp(char c) {
        return 'Z' == c ? 'A' : (char) (c + 1);
    }

    /**
     * Shifts Characters down, B to A etc
     * @param c the character to shift
     * @return the shifted character
     */
    private char shiftDown(char c) {
        return 'A' == c ? 'Z' : (char) (c - 1);
    }

}
