package Maze;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Game class, which stores all of the game's information and player movement logic.
 * @author michael tang 300490290
 */
public class Game {

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private Board board;
    private Player player;
    private Set<AbstractActor> computerPlayers;
    private List<Integer> tickTiming = new ArrayList<>();

    private boolean levelCompleted = false;

    /**
     * The constructor of the Game
     * initialises the board of the game.
     * initialises all of the board object that it stores
     * @param board The board of the game
     * @param player The player playing the game
     * @param computerPlayers The enemies of the game.
     */
    public Game(Board board, Player player, Set<AbstractActor> computerPlayers) {
        //GUI calls the persistence and sends the Game object the necessary files
        this.board = board;
        this.player = player;
        this.computerPlayers = computerPlayers;

        for(int i = 0; i < this.computerPlayers.size(); i++){
            tickTiming.add(0);
        }
    }

    /**
     * Moves the enemies according to their tick rate.
     * If the enemies' tick rate has been reach then allow them to move.
     */
    public void moveEnemies() {
        if(computerPlayers.isEmpty()) return;

        int count = 0;
        for(AbstractActor c : computerPlayers) {
            if(c.getTickRate() == tickTiming.get(count)){
                c.move(player, board);
                tickTiming.set(count, 0);
            } else {
                tickTiming.set(count, tickTiming.get(count)+1);
            }
            count++;
        }
    }

    /**
     * The player interacts with the block of the direction of their position.
     * The player moves in that direction if possible.
     * @param direction The direction of the position that the player's
     *                 current position is interacting with/moving towards.
     */
    public void movePlayer(DIRECTION direction) {

        Position newPos;
        switch (direction) {
            case UP:
                newPos = new Position(player.getPos(), DIRECTION.UP);
                break;
            case DOWN:
                newPos = new Position(player.getPos(), DIRECTION.DOWN);
                break;
            case LEFT:
                newPos = new Position(player.getPos(), DIRECTION.LEFT);
                player.flipLeftImage(); //Changes the player image direction
                break;
            case RIGHT:
                newPos = new Position(player.getPos(), DIRECTION.RIGHT);
                player.flipRightImage(); //Changes the player image direction
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }
        assert (newPos.getX() >= 0 &&
                newPos.getX() <= board.getMap().length - 1 &&
                newPos.getY() >= 0 &&
                newPos.getY() <= board.getMap()[0].length - 1)
                : "New position is out of bounds.";
        assert (board.getMap()[newPos.getX()][newPos.getY()] != null)
                : "Position at array is null. If you're here then something really bad happened...";

        //Interact with the square and move there if possible.
        AbstractTile moveToTile = board.getMap()[newPos.getX()][newPos.getY()];
        if(moveToTile.interact(player)) {
            //Unlock the exit lock if all treasures have been collected
            if (allTreasuresCollected()){
                unlockExitLock();
            } else {
                lockExitLock();
            }

            player.getPos().move(direction);    //Move the player
        }

        if(moveToTile instanceof ExitPortal) {
            levelCompleted = true;
        }

    }

    /**
     * Locks the exit locks.
     * Mainly used for when the stalker enemy steals the player's treasure
     */
    private void lockExitLock(){
        for (int i = 0; i < board.getMap().length; i++) {
            for (int j = 0; j < board.getMap()[0].length; j++) {
                if(board.getMap()[i][j] instanceof ExitLock){
                    ExitLock tile = (ExitLock) board.getMap()[i][j];
                    tile.unChange();
                }
            }
        }
    }

    /**
     * Unlocks the Exit lock.
     * Used when player collects all of their treasures.
     */
    private void unlockExitLock() {
        for (int i = 0; i < board.getMap().length; i++) {
            for (int j = 0; j < board.getMap()[0].length; j++) {
                if(board.getMap()[i][j] instanceof ExitLock){
                    ExitLock tile = (ExitLock) board.getMap()[i][j];
                    tile.unlock();
                }
            }
        }
    }

    /**
     * Tells if all treasures have been collected.
     * @return Returns true if all treasures have been collected, false if not.a
     */
    public boolean allTreasuresCollected() {
        return treasuresLeft() == 0;
    }

    /**
     * Finds the number of treasures that are still uncollected.
     * @return Returns the number of uncollected treasures.
     */
    public int treasuresLeft(){
        int treasuresLeft = 0;
        for (int i = 0; i < board.getMap().length; i++) {
            for (int j = 0; j < board.getMap()[0].length; j++) {
                if(board.getMap()[i][j] instanceof Treasure) {
                    Treasure treasure = (Treasure)board.getMap()[i][j];
                    if (!treasure.isPickedUp()){
                        treasuresLeft++;
                    }
                }
            }
        }
        return treasuresLeft;
    }

    /**
     * Returns the board being used for the game.
     * @return Returns the board being used for the game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the player in the game.
     * @return Returns the player in the game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the enemies of the game.
     * @return Returns the enemies of the game.
     */
    public Set<AbstractActor> getComputerPlayers() {
        return computerPlayers;
    }

    /**
     * Tells whether or not the level has been completed.
     * @return Returns true if the level has been completed.
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }
}
