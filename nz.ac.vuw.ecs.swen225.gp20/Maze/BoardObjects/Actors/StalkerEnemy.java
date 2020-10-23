package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.BoardObjects.Tiles.*;
import Maze.Position;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The stalker enemy which chases the player and steals their treasures/keys.
 * @author michael tang 300490290
 */
public class StalkerEnemy extends AbstractActor {

    /**
     * The stalker enemy constructor.
     * @param position the position of the actors
     * @param tickRate the speed of the enemy
     */
    public StalkerEnemy(Position position, int tickRate) {
        super(position, tickRate);
        images.put("Enemy2", Toolkit.getDefaultToolkit().getImage("Resources/actors/Enemy2.png"));
        images.put("Enemy2Flipped", Toolkit.getDefaultToolkit().getImage("Resources/actors/Enemy2Flipped.png"));
        currentImage = images.get("Enemy2");
    }

    /**
     * Uses pathfinding to move 1 step closer to the player
     * @param player The player
     * @param board The board
     */
    @Override
    public void move(Player player, Board board) {
        if(player.getKeys().isEmpty() && player.getTreasures().isEmpty()){
            return;
        }

        Fringe path = findPath(player, board);
        //If path is null then there is no path current available path to the player
        if(path == null) {
            return;
        }
        //If path's previous node is null then the enemy is on top of the player
        if(path.getPrevious() == null || path.getPrevious().getPrevious() == null) {
            interact(player); //Interact with the player
            this.position = startingPos;
            return;
        }
        //Getting the second to last tile before current position to move to
        while(path.getPrevious().getPrevious() != null) {
            path = path.getPrevious();
        }

        Position newPos = board.findPosInBoard(path.getCurrent()); //Get the new position

        //Changes the actor image direction
        if(newPos.getX() >= position.getX()){
            currentImage = images.get("Enemy2");
        } else {
            currentImage = images.get("Enemy2Flipped");
        }

        position = newPos;
    }

    /**
     * Returns a Fringe that contains previous Fringes leading back to this (from the player)
     * @param player .
     * @param board .
     * @return Returns a Fringe that contains previous Fringes leading back to this (from the player)
     */
    private Fringe findPath(Player player, Board board) {
        Queue<Fringe> fringeQueue = new ArrayDeque<>(); //A Queue to search through all the tiles
        fringeQueue.offer(new Fringe(board.getMap()[position.getX()][position.getY()], null));  //Give the
        // actor's current position to find to the player
        Set<AbstractTile> visited = new HashSet<>();

        //Go through all the tiles' neighbours (North, East, West, South) and check if they've been visited
        while (!fringeQueue.isEmpty()) {
            Fringe current = fringeQueue.poll();
            AbstractTile node = current.getCurrent();
            //Checks if the node has already been visited, if not then visit it.
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

    /**
     * The enemy "robs" the player and sends their keys/treasures back to their starting position.
     * @param player The player.
     */
    @Override
    public void interact(Player player) {
        if(!player.getKeys().isEmpty()){
            List<Key> keys = player.getKeys();
            Key key = keys.get(0);
            keys.remove(key);
            key.unChange();
        } else if(!player.getTreasures().isEmpty()){
            Set<Treasure> treasures = player.getTreasures();
            Treasure treasure = null;
            for(Treasure t: treasures){
                treasure = t;
                break;
            }
            assert treasure != null:"Treasure is null! Somehow the player owns this object!?";
            treasures.remove(treasure);
            treasure.unChange();
        }
    }

    /**
     * Returns a set of abstract tiles of a certain tile, can set up the rules of where this actor can and can't go
     * @param tile Returns neighbours of this tile
     * @param board .
     * @return Returns a set of abstract tiles of a certain tile,
     *         can set up the rules of where this actor can and can't go
     */
    private Set<AbstractTile> findNeighbours(AbstractTile tile, Board board) {
        AbstractTile[][] map = board.getMap();
        Position position = board.findPosInBoard(tile);

        Set<AbstractTile> neighbours = new HashSet<>();

        assert position != null;

        //Checking if the tile isn't a side tile, and creates the rules of where the enemy can go
        if(position.getX() != 0) {
            AbstractTile left = map[position.getX() - 1][position.getY()];
            //If the tile isn't a wall, or a exit lock or locked door.
            if(!(left instanceof Wall) && !(left instanceof DeathTile) && !(left instanceof LockedDoor && ((LockedDoor) left).isLocked()) ) {
                neighbours.add(left);
            }
        }

        if(position.getX() != map.length-1) {
            AbstractTile right = map[position.getX() + 1][position.getY()];
            if(!(right instanceof Wall) && !(right instanceof DeathTile) && !(right instanceof LockedDoor && ((LockedDoor) right).isLocked()) ) {
                neighbours.add(right);
            }
        }

        if(position.getY() != 0) {
            AbstractTile up = map[position.getX()][position.getY() - 1];
            if(!(up instanceof Wall)&& !(up instanceof DeathTile) && !(up instanceof LockedDoor && ((LockedDoor) up).isLocked()) ) {
                neighbours.add(up);
            }
        }

        if(position.getY() != map[position.getX()].length - 1) {
            AbstractTile down = map[position.getX()][position.getY() + 1];
            if(!(down instanceof Wall) && !(down instanceof DeathTile) && !(down instanceof LockedDoor && ((LockedDoor) down).isLocked()) ) {
                neighbours.add(down);
            }
        }

        return neighbours;
    }
}
