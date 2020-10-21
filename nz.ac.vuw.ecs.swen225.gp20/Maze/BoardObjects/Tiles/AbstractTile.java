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
    protected boolean changed = false;

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

    /**
     * Sets the tile to its changed state.
     * Used for when loading in a saved level.
     * Doesn't change state by default.
     */
    public void setChangedTile(){
        changed = true;
    }

    public void unChange() {
        changed = false;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isRotated() {
        return rotated;
    }

    public Image getCurrentImage() {
        return currentImage;
    }
}
