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

    protected final Position startingPos;
    protected Position position;
    protected Map<String, Image> images = new HashMap<>();
    protected Image currentImage;
    protected final int tickRate;

    /**
     * Basic constructor for any actors
     * @param position The position of the actors.
     * @param tickRate The tick rate of the actors.
     */
    public AbstractActor(Position position, int tickRate) {
        this.position = position;
        startingPos = position.getPositionCopy();
        this.tickRate = tickRate;
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

    public Image getCurrentImage() {
        return currentImage;
    }

    public Position getStartingPos() {
        return startingPos;
    }

    public int getTickRate() {
        return tickRate;
    }
}
