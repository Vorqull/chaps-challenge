package RecordAndReplay;

import Maze.BoardObjects.Actors.AbstractActor;
import Persistence.Persistence;
import RecordAndReplay.Actions.Action;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is JUST like Reader, Recorder, and Writer. A list of simple helper methods for RecordAndReplay.
 * The Replayer's job is mostly to detect when the player moves forwards "or backwards" and replicate
 * the moves accordingly.
 *
 * REMINDER TO SELF: this is why map updates must be recorded too, so they can be reversed!!!
 *
 */
public class Replayer {
    private ArrayList<Recorder.Change> recordedChanges = new ArrayList<Recorder.Change>();
    private int level;
    private int startRecordingTimeStamp;
    private int playerStartX;
    private int playerStartY;
    private ArrayList<AbstractActor> enemies; //ONLY USED FOR ENEMY LOCATIONS

    private String loadStateLocation;

    private boolean pause = false;
    private boolean doubleSpeed = false;
    private int location = 0; //The location in the playback

    ArrayList<Change> prepedChanges = new ArrayList<Change>();

    /**
     * Used by Record and Replay.
     * The complicated json file reading stuff should be done in Reader.
     */
    public Replayer() {

    }

    /**
     * Spawns the separate window for controls
     */
    public void controlsWindow() {
        JFrame controlWindow = new JFrame("Replay Controls");

        JPanel mainPanel = new JPanel();

        Icon prevIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/prev.png");
        Icon playIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/play.png");
        Icon pauseIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/pause.png");
        Icon forwardIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/next.png");
        Icon doubleSpeedIcon = new ImageIcon(System.getProperty("user.dir") + "/Resources/replayButtons/doubleSpeed.png");

        JButton prev = new JButton(prevIcon);
        JButton play = new JButton(playIcon);
        JButton forward = new JButton(forwardIcon);
        JButton doubleSpeed = new JButton(doubleSpeedIcon);

        prev.setPreferredSize(new Dimension(50, 50));
        play.setPreferredSize(new Dimension(50, 50));
        forward.setPreferredSize(new Dimension(50, 50));
        doubleSpeed.setPreferredSize(new Dimension(50, 50));

        mainPanel.add(prev);
        mainPanel.add(play);
        mainPanel.add(forward);
        mainPanel.add(doubleSpeed);

        controlWindow.add(mainPanel);
        //APPLE
        controlWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        controlWindow.setLocation(500, 300);
        controlWindow.pack();

        controlWindow.setVisible(true);
    }

    //apply an action every time a tick happens
    public void tick() {
        Change current = prepedChanges.get(location);
        //current
    }

    public void doubleTickSpeed(boolean t) {
        doubleSpeed = t;
    }

    public void loadToStart() {
        Persistence.loadGame(level);
    }


    /** PREPS **/

    /**
     * Distributes same ticks across a 1000 milisecond tick evenly.
     * Adds additional "null" changes for ticks with nothing in 'em.
     */
    public void prepRecordedChanges() {
        Map<Integer, ArrayList<Recorder.Change>> rawFindings = new HashMap<Integer, ArrayList<Recorder.Change>>();
        //A: FIRST, Find ALL the same actions
        for(Recorder.Change c : recordedChanges) {
            if(!rawFindings.containsKey(c.timestamp)) {
                ArrayList<Recorder.Change> subjectList = new ArrayList<Recorder.Change>();
                subjectList.add(c);
                rawFindings.put(c.timestamp, subjectList);
            } else {
                ArrayList<Recorder.Change> subjectList = rawFindings.get(c.timestamp);
                subjectList.add(c);
            }
        }
        //B: SECOND, Create Replayer.Change, add to prepedChanges. Add nulls if need be.
        int start = recordedChanges.get(0).timestamp;
        int end = recordedChanges.get(recordedChanges.size()-1).timestamp;
        //Distribute same timestamped changes.
        for(int i = start; i > end; i--) {
            if(rawFindings.containsKey(i)) {
                int denominator = rawFindings.get(i).size();
                int factor = 1000 / denominator;
                int milisecond = 1000;
                ArrayList<Recorder.Change> entry = rawFindings.get(i);
                for(Recorder.Change c : entry) {
                    prepedChanges.add(new Change(c.actions, i, milisecond));
                    milisecond -= factor;
                }
            } else {
                prepedChanges.add(new Change(null, i, 1000));
            }
        }
    }

    /** SETTERS **/
    public void setRecordedChanges(ArrayList<Recorder.Change> recordedChanges) {
        this.recordedChanges = recordedChanges;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStartRecordingTimeStamp(int startRecordingTimeStamp) {
        this.startRecordingTimeStamp = startRecordingTimeStamp;
    }

    public void setPlayerStartX(int playerStartX) {
        this.playerStartX = playerStartX;
    }

    public void setPlayerStartY(int playerStartY) {
        this.playerStartY = playerStartY;
    }

    public void setEnemies(ArrayList<AbstractActor> enemies) {
        this.enemies = enemies;
    }

    public void setLoadState(String loadStateLocation) {
        this.loadStateLocation = loadStateLocation;
    }

    /** NESTED CLASSES **/
    /**
     * Almost exactly like Recorder's "Change" class.
     * Only this one has it's own milisecond tick variable.
     */
    private class Change {
        public final ArrayList<Action> actions;
        public final int timestamp;
        public final int milisecondTick;

        public Change(ArrayList<Action> actions, int timestamp, int milisecondTick) {
            this.actions = actions; //can be null
            this.timestamp = timestamp;
            this.milisecondTick = milisecondTick;
        }
    }
}
