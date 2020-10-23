package Maze.BoardObjects.Actors;

import Maze.Board;
import Maze.Position;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A abstract class that represents any movable objects on the board.
 * @author michael tang 300490290
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

    /**
     * The AI for moving the actor, it also defines
     * when the actor wants to interact with the player
     * @param player The player that is on the same board same this
     * @param board The board that this actor is on
     */
    public abstract void move(Player player, Board board);

    /**
     * The AI for interacting with the player.
     * @param player The player that the actor is interacting with.
     */
    public abstract void interact(Player player);

    /**
     * Returns the position of the actor.
     * @return .
     */
    public Position getPos() {
        return position;
    }

    /**
     * Sets the position of the actor.
     * @param pos The position being set to.
     */
    public void setPos(Position pos){
        this.position = pos;
    }

    /**
     * Gets the image that the actor is currently using
     * @return Returns the current image of the actor.
     */
    public Image getCurrentImage() {
        return currentImage;
    }

    /**
     * Returns the starting position of the actor.
     * @return Returns the starting position of the actor.
     */
    public Position getStartingPos() {
        return startingPos;
    }

    /**
     * Returns the tick rate of the actor.
     * @return Returns the tick rate of the actor.
     */
    public int getTickRate() {
        return tickRate;
    }
}
