package Maze;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.*;
import RecordAndReplay.RecordAndReplay;

import java.util.Set;

public class Game {

    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private Board board;
    private Player player;
    private Set<AbstractActor> computerPlayers;

    public Game(Board board, Player player, Set<AbstractActor> computerPlayers) {
        //GUI calls the persistence and sends the Game object the necessary files
        this.board = board;
        this.player = player;
        this.computerPlayers = computerPlayers;
    }

    public void moveEnemyCheckWin() {
        for(AbstractActor c : computerPlayers) {
            c.move(player, board);
        }
    }

    /**
     * The player interacts with the block of the direction of their position.
     * The player moves in that direction if possible.
     * @param direction The direction of the position that the player's
     *                 current position is interacting with/moving towards.
     */
    public void movePlayer(DIRECTION direction) {

        ////////TEST CODE
        for(AbstractActor a : computerPlayers) {
            System.out.println("Enemy: ");
            a.move(player, board);
            System.out.println(a.getPos());
            a.move(player, board);
            System.out.println(a.getPos());
            a.move(player, board);
            System.out.println(a.getPos());
        }
        //////


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
                break;
            case RIGHT:
                newPos = new Position(player.getPos(), DIRECTION.RIGHT);
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

        /**
        //CHECK IF MOVING INTO ENEMY
        for(AbstractActor enemy : computerPlayers) {
            if(enemy.getPos().equals(newPos)){
                enemy.interact(player);
            }
        }
         **/

        //Interact with the square and move there if possible.
        AbstractTile moveToTile = board.getMap()[newPos.getX()][newPos.getY()];
        if(moveToTile.interact(player)) {
            //Unlock the exit lock if all treasures have been collected
            if (allTreasuresCollected()){
                unlockExitLock();
            }

            player.getPos().move(direction);    //Move the player
        }

        ////////// TEST CODE
        System.out.println("Player: ");
        System.out.println(player.getPos());
        ////////////////
    }

    private void unlockExitLock() {
        for (int i = 0; i < board.getMap().length; i++) {
            for (int j = 0; j < board.getMap()[0].length; j++) {
                if(board.getMap()[i][j] instanceof ExitLock){
                    board.getMap()[i][j] = new FreeTile();
                }
            }
        }
    }

    private boolean allTreasuresCollected() {
        for (int i = 0; i < board.getMap().length; i++) {
            for (int j = 0; j < board.getMap()[0].length; j++) {
                if(board.getMap()[i][j] instanceof Treasure) {
                    Treasure treasure = (Treasure)board.getMap()[i][j];
                    if (!treasure.isPickedUp()){
                        return false;
                    }
                }
            }
        }
        return true;
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
}
