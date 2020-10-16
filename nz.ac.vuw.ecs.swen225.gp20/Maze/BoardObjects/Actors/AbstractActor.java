package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.Position;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A abstract class that represents any movable objects on the board.
 */
public abstract class AbstractActor {

    protected Position position;
    protected Map<String, Image> images = new HashMap<>();
    protected Image currentImage;

    /**
     * Basic constructor for any actors
     * @param position the position of the actors
     */
    public AbstractActor(Position position) {
        this.position = position;
    }

    public abstract void move(Player player, Board board);

    public abstract void interact(Player player);

    /**
     * Returns the position of the actor.
     * @return .
     */
    public Position getPos() {
        return position;
    }

}
