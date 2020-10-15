package Maze.BoardObjects.Actors.stalker_enemy;

import Maze.BoardObjects.Tiles.AbstractTile;

public class Fringe {

    private AbstractTile current;
    private Fringe previous;

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
