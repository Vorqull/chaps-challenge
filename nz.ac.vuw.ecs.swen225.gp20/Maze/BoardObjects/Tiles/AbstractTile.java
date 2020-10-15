package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Tile interface,
 * all other tiles object will use the methods
 * within this abstract class.
 */
public abstract class AbstractTile {

    protected boolean rotated = false;
    protected Map<String, Image> images = new HashMap<>();
    protected Image currentImage;

    /**
     * .
     * @param setVertical tells if the tile is vertical or horizontal
     */
    public AbstractTile(boolean setVertical) {
        this.rotated = setVertical;
    }

    /**
     * The tile interacts with the player,
     * returning true if the player can move on to the tile
     * and false if they can't.
     * @param player The player that interacts with the tile.
     * @return Returns true if the actor can move on to this tile, false if not.
     */
    public abstract boolean interact(Player player);

    public boolean isRotated() {
        return rotated;
    }

    public Image getCurrentImage() {
        return currentImage;
    }
}
