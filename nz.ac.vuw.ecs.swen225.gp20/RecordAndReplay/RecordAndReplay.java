package RecordAndReplay;

import Maze.Board;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.Game;
import Maze.Game.DIRECTION;
import Maze.Position;
import Persistence.EnemyBlueprint;
import Persistence.Level;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Records gameplay.
 * + Stores recorded games in a JSON format file.
 *   - Chap's movement
 *   - Any and every other actors
 * + Can also load a recorded game and replay it.
 * + User should have controls for replay:
 *   - Step-by-step,      -(ASSUMPTION)iterate through every action
 *                        -(ASSUMPTION)iterate through every tick
 *                        -(ASSUMPTION)Player can move forward and backward through the recording.
 *   - auto-reply,        -(ASSUMPTION)Play through normal tick speed
 *   - set replay speed.  -(ASSUMPTION)Set tick speed depending on seconds.
 * + (ASSUMPTION) Player CANNOT undo or redo.
 *
 * ////////////////////////////////////////////////////////////////
 * On a completely separate tick cycle, enemies will move on their own.
 * Every move, it checks if they have interacted with player.
 *
 * Multiple moves (aka changes) can happen at once.
 * Every change should be an array of changes.
 * The recording should be an array, of the array of changes.
 * ////////////////////////////////////////////////////////////////
 *
 * INDEX:
 * > RECORDING
 * > SAVING
 * > LOADING
 * > PLAYING
 * > GETTERS/SETTERS
 */
public class RecordAndReplay<E> {
    private int level; //the Level the game is associated with. It's a string because that's how persistence works.
    private Recorder recorder;
    private Writer writer;
    private boolean recordingSwitch;
    private int startedRecording;
    private EnemyBlueprint[] enemies;

    /**
     * Constructor with level parameter
     * @param level The level number which is associated with the RecordAndReplayer
     * @param enemies The list of enemies in this level
     */
    public RecordAndReplay(int level, EnemyBlueprint[] enemies) {
        recorder = new Recorder();
        this.writer = new Writer();
        recordingSwitch = false;
        this.level = level;
        this.enemies = enemies;
    }

    /**
     * Parameterless constructor.
     */
    public RecordAndReplay() {
        recorder = new Recorder();
        this.writer = new Writer();
        recordingSwitch = false;
    }

    //=====RECORDER=====//
    //Returns the current state of the switch, and also flips it.
    public boolean getRecordingBoolean() {
        return recordingSwitch;
    }
    public void setRecordingBoolean(Boolean s) {
        recordingSwitch = s;
    }

    //Set the starting position. I COULD have put it in the above method, but I dont wanna seem like a sociopath.
    public void setStartingPosition(Position pos) {
        recorder.setStartingPosition(pos);
    }

    //Effectively relays all the recorder's functions here. Doing this to save me from headache.
    public void capturePlayerMove(Game.DIRECTION direction) {
        recorder.capturePlayerMove(direction);
    }
    public void captureTileInteraction(AbstractTile tile) {
        recorder.captureTileInteraction(tile);
    }

    //Note when recording started
    public void setStartedRecording(int timestamp) {
        startedRecording = timestamp;
    }

    //DO THIS AT THE END OF ALL CAPTURES
    public void clearRecorderBuffer(int timestamp) {
        //deletes the recording buffer if it shouldnt be recording.
        if(recordingSwitch) recorder.storeBuffer(timestamp);
        else recorder.deleteBuffer();
    }

    //=====SAVING=====//  AKA WRITING
    //All functions to do with creating a save via JSON is here.
    public void saveGameplay() {
        writer.writeRecording(recorder.getRecordedChanges(), recorder.getStartingPosition(), level, startedRecording, enemies);
    }

    //=====LOADING=====//
    //All functions to do with loading the game
    public boolean loadConfirmation(JFrame frame) {
        int selection = JOptionPane.showConfirmDialog(frame, "WARNING: Loading a replay will quit out of your current game.\n" +
                "Proceed?", "Load Replay Confirmation", JOptionPane.YES_NO_OPTION);
        if(selection == 1) return true;
        else return false;
    }

    //=====PLAYING=====//
    //All functions to do with replaying, forward or backwards.

    //=====GETTERS/SETTERS=====//

    /**
     * Set the name of the json file this recorded session will be associated with.
     * @param level
     */
    public void setLevelName(int level) {
        this.level = level;
    }
}
