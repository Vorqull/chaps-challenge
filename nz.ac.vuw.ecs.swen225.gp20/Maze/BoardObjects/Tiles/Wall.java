package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The wall tile of the game. block the player from walking to places
 * @author michael tang 300490290
 */
public class Wall extends AbstractTile {

    /**
     * The Constructor for wall tiles.
     */
    public Wall() {
        super(false);
        images.put("WallTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/WallTile.jpeg"));
        currentImage = images.get("WallTile");
    }

    /**
     * Always returns false because player can't move through this tile.
     * @param player The player that is interacting with this tile.
     * @return Returns false for walls because nothing can move through this tile.
     */
    @Override
    public boolean interact(Player player) {
        return false;
    }
}
