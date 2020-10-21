package RecordAndReplay;

import Persistence.EnemyBlueprint;
import RecordAndReplay.Actions.Action;

import javax.swing.*;
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
    private ArrayList<EnemyBlueprint> enemies; //ONLY USED FOR ENEMY LOCATIONS

    ArrayList<Change> prepedChanges = new ArrayList<Change>();

    /**
     * Used by Record and Replay.
     * The complicated json file reading stuff should be done in Reader.
     */
    public Replayer() {

    }

    /**
     * Spawns the separate window for controls
     * @param frame Parent frame.
     */
    public void controlsWindow(JFrame frame) {
        JDialog controlsWindow = new JDialog(frame);
        controlsWindow.setAlwaysOnTop(true);
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
        for(int i = start; i > end; i--) {
            if(rawFindings.containsKey(i)) {
                int denominator = rawFindings.get(i).size();
                int factor = 1000 / denominator;
                int milisecond = 1000;
                ArrayList<Recorder.Change> entry = rawFindings.get(start-i);
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

    public void setEnemies(ArrayList<EnemyBlueprint> enemies) {
        this.enemies = enemies;
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
