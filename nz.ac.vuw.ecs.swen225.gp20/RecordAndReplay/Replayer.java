package RecordAndReplay;

import RecordAndReplay.Actions.Action;

import java.util.ArrayList;

/**
 * This class is JUST like Reader, Recorder, and Writer. A list of simple helper methods for RecordAndReplay.
 * The Replayer's job is mostly to detect when the player moves forwards "or backwards" and replicate
 * the moves accordingly.
 *
 * REMINDER TO SELF: this is why map updates must be recorded too, so they can be reversed!!!
 *
 */
public class Replayer {
    ArrayList<Recorder.Change> listOfMoves;

    /**
     * Used by Record and Replay.
     * The complicated stuff should be done in Reader.
     */
    public void fillListOfMoves (ArrayList<Recorder.Change> listOfMoves) {
        this.listOfMoves = listOfMoves;
    }
}
