package Maze;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import RecordAndReplay.RecordAndReplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private Board board;
    private Player player;
    private Set<AbstractActor> computerPlayers;

    private boolean levelCompleted = false;

    public Game(Board board, Player player, Set<AbstractActor> computerPlayers) {
        //GUI calls the persistence and sends the Game object the necessary files
        this.board = board;
        this.player = player;
        this.computerPlayers = computerPlayers;
    }

    public void moveEnemy() {
        List<Integer> tickTiming = new ArrayList<>();
        for(int i = 0; i < computerPlayers.size(); i++){
            tickTiming.add(0);
        }

        int count = 0;
        for(AbstractActor c : computerPlayers) {
            if(c.getTickRate() == tickTiming.get(count)){
                c.move(player, board);
                tickTiming.set(count, 0);
            }
            tickTiming.set(count, tickTiming.get(count)+1);
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
            }

            player.getPos().move(direction);    //Move the player
        }

        if(moveToTile instanceof ExitPortal) {
            levelCompleted = true;
        }

        ////////TEST CODE
        int count = 0;
        for(AbstractActor a : computerPlayers) {
            System.out.println("Enemy " + count + ": ");
            a.move(player, board);
            System.out.println(a.getPos());
            //a.move(player, board);
            //System.out.println(a.getPos());
            count++;
        }
        //////

    }

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

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public Set<AbstractActor> getComputerPlayers() {
        return computerPlayers;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
}
