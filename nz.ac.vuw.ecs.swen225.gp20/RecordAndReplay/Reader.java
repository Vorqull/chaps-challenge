package RecordAndReplay;

import Maze.Position;
import Persistence.EnemyBlueprint;
import RecordAndReplay.Actions.Action;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Should be able to read a json file.
 * Upon bad formatting, closes with a failure state.
 * When successful, should be able to report an arraylist of arraylists of actions.
 *
 * Reminder to self:
 * So Essentially, the player should select when they wanna start a replay.
 * Warn the player: their current game session will end.
 * They proceed, All the created saves should be presented in the next alert box. (gives an error if there is none and returns to their game)
 * They select a save, The json save should be loaded, via this reader, into the recorded changes. (gives an error on bad formatting and returns to their game)
 * Immediately resets the game, and loads the player in their beginning spot OF said recording (NOTE: you'll also need to deploy creatures
 * in their beginning spots too!)
 * All this class does is return "recordedChanges" however.
 *
 * In "Replayer" (recordedChanges should be loaded in RecordAndReplay) the player may use arrow keys to go left or right in the recording.
 * (should also reveal a "controls" menu item. displays a window that lists the controls for the replayer)
 * If there is no more recording, an alert should display "END OF RECORDING".
 * If the player rewinds too far, an alert shouldn't display but the Replayer shouldn't allow it to rewind.
 * Player should also allow an "auto replay" feature. (every action now requires a time stamp which actually requires a game clock to be implemented...)
 */
public class Reader {
    private ArrayList<Recorder.Change> recordedChanges = new ArrayList<Recorder.Change>();
    private int level;
    private int startRecordingTimeStamp;
    private int playerStartX;
    private int playerStartY;
    private ArrayList<EnemyBlueprint> enemies; //ONLY USED FOR ENEMY LOCATIONS

    public Reader() {
        //empty constructor
    }

    public void readJson(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        }
        JsonReader reader = Json.createReader(inputStream);
        JsonObject obj = reader.readObject();
        level = obj.getInt("Level");
        startRecordingTimeStamp = obj.getInt("startRecordingTimeStamp");
        JsonObject playerPos = obj.getJsonObject("playerPos");
        playerStartX = playerPos.getInt("startX");
        playerStartY = playerPos.getInt("startY");
        //Insert something to do with enemies HERE

        //CHANGE ARRAY!!!
        int noChanges = obj.getInt("noChanges");
        for(int i = 0; i < noChanges; i++) {
            int actual = i + 1;
            JsonObject changeList = obj.getJsonObject("change" + actual);

            //Get timestamp
            int timeStamp = changeList.getInt("Timestamp");

            //Get the array of actions
            ArrayList<Action> actions = new ArrayList<Action>();
            for(int j = 0; j < changeList.size()-1; j++) {
                //FIRST get object
                JsonObject action = changeList.getJsonObject("" + j);

                //SECOND check type (brute force)
                if(action.get("PlayerMove") == null) System.out.println("works");
                else System.out.println("also works");
            }

            //Recorder.Change c = new Recorder.Change();
        }
    }

    /** HELPER METHODS **/
}
