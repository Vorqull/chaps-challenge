package Maze.BoardObjects.Actors;

import Maze.BoardObjects.Tiles.AbstractTile;

/**
 * A fringe used for pathfinding of the stalker enemy.
 * @author michael tang 300490290
 */
public class Fringe {

    private final AbstractTile current;
    private final Fringe previous;

    /**
     * THe constructor of the fringe containing a current tile
     * and its previous Fringe.
     * @param current THe tile its holding
     * @param previous THe fringe preceding this fringe.
     */
    public Fringe(AbstractTile current, Fringe previous){
        this.current = current;
        this.previous = previous;
    }

    /**
     * Gets the tile of this fringe
     * @return Returns the tile of this fringe.
     */
    public AbstractTile getCurrent() {
        return current;
    }

    /**
     * Gets the fringe preceding this fringe
     * @return Returns the previous fringe of the this fringe.
     */
    public Fringe getPrevious() {
        return previous;
    }
}
