package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The exit portal, which indicates the end goal of a stage
 * @author michael tang 300490290
 */
public class ExitPortal extends AbstractTile {
    /**
     * The constructor of the exit portal.
     */
    public ExitPortal() {
        super(false);
        images.put("Vent", Toolkit.getDefaultToolkit().getImage("Resources/tiles/Vent.jpeg"));
        currentImage = images.get("Vent");
    }

    /**
     * Lets the player move onto it like a free tile
     * @param player The player that interacts with the tile.
     * @return Returns true.
     */
    @Override
    public boolean interact(Player player) {
        return true;
    }

}
