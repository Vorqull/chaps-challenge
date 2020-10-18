package Maze.BoardObjects.Actors.stalker_enemy;

import Maze.BoardObjects.Tiles.AbstractTile;

public class Fringe {

    private final AbstractTile current;
    private final Fringe previous;

    public Fringe(AbstractTile current, Fringe previous){
        this.current = current;
        this.previous = previous;
    }

    public AbstractTile getCurrent() {
        return current;
    }

    public Fringe getPrevious() {
        return previous;
    }
}
