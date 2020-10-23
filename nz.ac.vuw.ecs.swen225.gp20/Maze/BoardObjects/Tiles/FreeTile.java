package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The "empty" tile on the board, any actor can move freely onto this tile.
 * @author michael tang 300490290
 */
public class FreeTile extends AbstractTile {

    /**
     * The constructor of the free tile.
     */
    public FreeTile() {
        super(false);
        images.put("FloorTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"));
        currentImage = images.get("FloorTile");
    }


    /**
     * The free tile, which allows the players to be on top of it.
     * @param player The player that interacts with the tile.
     * @return Returns true.
     */
    @Override
    public boolean interact(Player player) {
        return true;
    }
}
