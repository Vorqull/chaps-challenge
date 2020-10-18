package RecordAndReplay;

import RecordAndReplay.Actions.Action;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
    ArrayList<ArrayList<Action>> recordedChanges = new ArrayList<ArrayList<Action>>();
    public Reader() {
        //empty constructor
    }

    //goes through all the objects in "saves" and
    //ANOTHER REMINDER TO SELF: players should be able to name their saves
    /**
     * This helps find all the save jsons in the package and presents them.
     */
    public File[] findSaves() {
        ArrayList<File> returnThis = new ArrayList<File>();
        File directory = new File(System.getProperty("user.dir") + "/nz.ac.vuw.ecs.swen225.gp20/RecordAndReplay/Saves");
        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File directory, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });

        return files;
    }

    //read json

    /** HELPER METHODS **/
}
