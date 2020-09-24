package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.BoardObject;
import Maze.Position;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of the Tile interface,
 * all other tiles object will use the methods
 * within this abstract class.
 */
public abstract class AbstractTile implements BoardObject {

    public final Position position; //Final cause tiles don't move to different places (although they can be picked up)
    protected boolean rotated = false;
    protected Map<String, Image> images = new HashMap<>();
    protected Image currentImage;

    /**
     * .
     * @param position .
     */
    public AbstractTile(Position position, boolean setVertical) {
        this.position = position;
        this.rotated = setVertical;
    }

    /**
     * The tile interacts with the player,
     * returning true if the player can move on to the tile
     * and false if they can't.
     * @param player The player that interacts with the tile.
     * @return Returns true if the actor can move on to this tile, false if not.
     */
    public boolean interact(Player player){
        return true;
    }

    public boolean isRotated() {
        return rotated;
    }
}