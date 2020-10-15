package Maze.BoardObjects.Actors.stalker_enemy;

import Maze.Board;
import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.AbstractTile;
import Maze.BoardObjects.Tiles.ExitLock;
import Maze.BoardObjects.Tiles.LockedDoor;
import Maze.BoardObjects.Tiles.Wall;
import Maze.Position;

import java.util.*;

public class StalkerEnemy extends AbstractActor {

    /*
     * @param position the position of the actors
     */
    public StalkerEnemy(Position position) {
        super(position);
    }

    @Override
    public void move(Player player, Board board) {
        Fringe path = findPath(player, board);
        //If path is null then there is no path current available path to the player
        if(path == null) {
            return;
        }
        //If path's previous node is null then the enemy is on top of the player
        if(path.getPrevious() == null) {
            //interact(player); //? maybe?
            return;
        }

        while(path.getPrevious().getPrevious() != null) {
            path = path.getPrevious();
        }

        position = board.findPosInBoard(path.getCurrent());
    }

    private Fringe findPath(Player player, Board board) {
        Queue<Fringe> fringeQueue = new ArrayDeque<>();
        fringeQueue.offer(new Fringe(board.getMap()[position.getX()][position.getY()], null));
        Set<AbstractTile> visited = new HashSet<>();

        while (!fringeQueue.isEmpty()) {
            Fringe current = fringeQueue.poll();
            AbstractTile node = current.getCurrent();

            if(!visited.contains(node)) {
                visited.add(node);

                //Found the shortest path
                if (board.findPosInBoard(node).equals(player.getPos())) {
                    return current;
                }

                for(AbstractTile n : findNeighbours(node, board)) {
                    if(!visited.contains(n)) {
                        fringeQueue.offer(new Fringe(n, current));
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void interact(Player player) {

    }

    private Set<AbstractTile> findNeighbours(AbstractTile tile, Board board) {
        AbstractTile[][] map = board.getMap();
        Position position = board.findPosInBoard(tile);

        Set<AbstractTile> neighbours = new HashSet<>();

        assert position != null;
        if(position.getX() != 0) {
            AbstractTile left = map[position.getX() - 1][position.getY()];
            //If the tile isn't a wall, or a exitlock or locked door.
            //NEED TO TEST LOCKED DOOR BOOLEAN IS DONE CORRECTLY
            if(!(left instanceof Wall) && !(left instanceof ExitLock) && !(left instanceof LockedDoor && ((LockedDoor) left).isLocked()) ) {
                neighbours.add(left);
            }
        }

        if(position.getX() != map.length-1) {
            AbstractTile right = map[position.getX() + 1][position.getY()];
            if(!(right instanceof Wall) && !(right instanceof ExitLock) && !(right instanceof LockedDoor && ((LockedDoor) right).isLocked()) ) {
                neighbours.add(right);
            }
        }

        if(position.getY() != 0) {
            AbstractTile up = map[position.getX()][position.getY() - 1];
            if(!(up instanceof Wall) && !(up instanceof ExitLock) && !(up instanceof LockedDoor && ((LockedDoor) up).isLocked()) ) {
                neighbours.add(up);
            }
        }

        if(position.getY() != map[position.getX()].length - 1) {
            AbstractTile down = map[position.getX()][position.getY() + 1];
            if(!(down instanceof Wall) && !(down instanceof ExitLock) && !(down instanceof LockedDoor && ((LockedDoor) down).isLocked()) ) {
                neighbours.add(down);
            }
        }

        return neighbours;
    }
}
