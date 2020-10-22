package RecordAndReplay;

import Application.ChapsChallenge;
import Maze.Board;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.BoardObjects.Tiles.Treasure;
import Maze.Game;
import Maze.Game.DIRECTION;
import Maze.Position;
import Persistence.*;

import javax.swing.*;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private Replayer replayer;
    private Reader reader;
    private boolean recordingSwitch;
    private int startedRecording;
    private ArrayList<AbstractActor> enemies;

    /**
     * Constructor with level parameter
     * @param level The level number which is associated with the RecordAndReplayer

    public RecordAndReplay(int level, Set<AbstractActor> enemies, int j) {
        recorder = new Recorder();
        writer = new Writer();
        replayer = new Replayer();
        reader = new Reader();
        recordingSwitch = false;
        this.level = level;
        this.enemies = new ArrayList<AbstractActor>();

        for(AbstractActor e : enemies) {
            this.enemies.add(e);
        }
    }*/

    /**
     * Parameterless constructor.
     */
    public RecordAndReplay(ChapsChallenge application) {
        recorder = new Recorder();
        writer = new Writer();
        replayer = new Replayer(application);
        reader = new Reader();
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
    public void capturePlayerMove(DIRECTION direction) {
        recorder.capturePlayerMove(direction);
    }
    public void captureTileInteraction(AbstractTile tile) {
        recorder.captureTileInteraction(tile);
    }

    //Finds ALL the enemies current positions.
    public void captureEnemyPreMoves(Set<AbstractActor> enemies) {
        recorder.captureEnemyPreMoves(enemies);
    }

    public void captureEnemyPostMoves(Set<AbstractActor> enemies) {
        recorder.captureEnemyPostMoves(enemies);
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
    public void saveGameplay(int remainingTime, Player player, Set<AbstractActor> enemies, AbstractTile[][] tiles) {
        ArrayList<AbstractActor> enemyList = new ArrayList<AbstractActor>();
        for(AbstractActor e : enemies) {
            enemyList.add(e);
        };

        writer.writeRecording(recorder.getRecordedChanges(), recorder.getStartingPosition(), level, startedRecording, enemyList);
    }

    //=====LOADING=====//
    //All functions to do with loading the game
    public boolean loadConfirmation(JFrame frame) {
        int selection = JOptionPane.showConfirmDialog(frame, "WARNING: Loading a replay will quit out of your current game.\n" +
                "Proceed?", "Load Replay Confirmation", JOptionPane.YES_NO_OPTION);
        if(selection == 0) return true;
        else return false;
    }

    /**
     * Allows the player to select a save file and immediately loads it upon selection.
     * @param frame The parent frame for the dialog box.
     */
    public void selectSaveFile(JFrame frame) {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/nz.ac.vuw.ecs.swen225.gp20/RecordAndReplay/Saves");

        int returnValue = jfc.showOpenDialog(frame);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedSaveFile = jfc.getSelectedFile();

            reader.readJson(selectedSaveFile);
            prepReplayer();
        }
    }

    //=====PLAYING=====//
    //All functions to do with replaying, forward or backwards.
    public void prepReplayer() {
        replayer.setRecordedChanges(reader.getRecordedChanges());
        replayer.setLevel(reader.getLevel());
        replayer.setLoadState(reader.getLevel());

        replayer.prepRecordedChanges();
        replayer.loadToStart();
    }

    public void tick() {
        replayer.tick();
    }

    public void displayControlWindow() {
        replayer.controlsWindow();
    }

    public boolean getPaused() {
        return replayer.isPaused();
    }

    //=====GETTERS/SETTERS=====//

    /**
     * Set the name of the json file this recorded session will be associated with.
     * @param level
     */
    public void setLevelCount(int level) {
        this.level = level;
    }

    /**
     * Set the arraylist of enemies that exist in this game.
     * Only useful for the writing module
     * @param enemies The set of enemies
     */
    public void setEnemies(Set<AbstractActor> enemies) {
        ArrayList<AbstractActor> e = new ArrayList<AbstractActor>();
        for(AbstractActor a : enemies) {
            e.add(a);
        }
        this.enemies = e;
    }
}
