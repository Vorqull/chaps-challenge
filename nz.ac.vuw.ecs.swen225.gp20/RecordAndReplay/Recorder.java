package RecordAndReplay;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.Key;
import Maze.Game;
import Maze.Position;
import RecordAndReplay.Actions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This handles all the recording that needs to take place in the game.
 * It's a "recorder" haha get it???
 * Also, it should be able to report "recordedChanges" to RecordAndReplay,
 * So that RecordAndReplay can pass it to "writer".
 *
 * NOTE: ONLY RecordAndReplay.java should have access to this!!!
 *
 */
public class Recorder {
    private List<Change> recordedChanges;
    private ArrayList<Action> changeBuffer;
    private Position startingPosition;
    private ArrayList<AbstractActor> preMoveEnemies = new ArrayList<AbstractActor>();
    private ArrayList<AbstractActor> postMoveEnemies = new ArrayList<AbstractActor>();

    public Recorder() {
        recordedChanges = new ArrayList<Change>();
        changeBuffer = new ArrayList<Action>();
    }

    /**LIST OF RECORD ACTIONS!!!!**/
    /**
     * Records a player movement and stores it in the buffer
     */
    public void capturePlayerMove(Game.DIRECTION direction) {
        Action addthis = new PlayerMove(direction);
        changeBuffer.add(addthis);
    }

    public void captureTileInteraction (AbstractTile tile) {
        Action addthis = new PlayerTileInteraction(tile);
        changeBuffer.add(addthis);
    }

    /** Records a creature movement and stores it in the buffer. */
    public void captureEnemyPreMoves(Set<AbstractActor> enemies) {
        for(AbstractActor e: enemies) {
            preMoveEnemies.add(e);
        }
    }
    public void captureEnemyPostMoves(Set<AbstractActor> enemies) {
        for(AbstractActor e: enemies) {
            postMoveEnemies.add(e);
        }
        for(int i = 0; i < preMoveEnemies.size(); i++) {
            //===Calculate directions here===//
            Game.DIRECTION direction = null;
            //FIRST find the positions
            Position preMovePos = preMoveEnemies.get(i).getPos();
            int preMoveX = preMovePos.getX();
            int preMoveY = preMovePos.getY();

            Position postMovePos = postMoveEnemies.get(i).getPos();
            int postMoveX = postMovePos.getX();
            int postMoveY = postMovePos.getY();

            //SECOND Brute force IF statements
            if(postMoveX < preMoveX) {
                direction = Game.DIRECTION.LEFT;
            } else if (preMoveX < postMoveX) {
                direction = Game.DIRECTION.RIGHT;
            } else if (postMoveY < preMoveY) {
                direction = Game.DIRECTION.UP;
            } else if (preMoveY < postMoveY) {
                direction = Game.DIRECTION.DOWN;
            }

            changeBuffer.add(new EnemyMove(preMoveX, preMoveY, direction));
        }
    }

    //Add moves as needed
    /** Clears the buffer, stores it into the recordedChanges array. */
    public void storeBuffer(int timestamp) {
        Change c = new Change(changeBuffer, timestamp);

        recordedChanges.add(c);
        changeBuffer = new ArrayList<Action>();
    }

    /** Clears the buffer, does NOT add it into the recordedChanges array. */
    public void deleteBuffer() {
        changeBuffer = new ArrayList<Action>();
    }

    public void setStartingPosition(Position pos) {
        startingPosition = pos;
    }
    public Position getStartingPosition() {
        return startingPosition;
    }

    public List<Change> getRecordedChanges() { return recordedChanges; }

    /**
     * Private nested class object stores both the list of actions
     * AND a time stamp to associate itself with.
     */
    public static class Change {
        public final ArrayList<Action> actions;
        public final int timestamp;

        public Change(ArrayList<Action> actions, int timestamp) {
            this.actions = actions;
            this.timestamp = timestamp;
        }
    }
}
