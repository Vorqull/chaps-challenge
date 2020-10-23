package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Tile interface,
 * all other tiles object will use the methods
 * within this abstract class.
 * @author michael tang 300490290
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

    /**
     * Change the tile back to its original state.
     */
    public void unChange() {
        changed = false;
    }

    /**
     * Returns whether or not the state of the tile has changed.
     * @return Returns whether or not the state of the tile has changed.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Returns whether or not the image is rotated.
     * @return Returns whether or not the image is rotated.
     */
    public boolean isRotated() {
        return rotated;
    }

    /**
     * Returns the current Image of the tile.
     * @return Returns the current image of the tile.
     */
    public Image getCurrentImage() {
        return currentImage;
    }
}
