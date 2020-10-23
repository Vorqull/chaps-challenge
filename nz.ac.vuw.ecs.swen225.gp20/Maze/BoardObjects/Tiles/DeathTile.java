package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;
import Maze.Position;

import java.awt.*;


/**
 * The instant death tile on the board, if the player falls on this tile they respawn to their starting point.
 * @author michael tang 300490290
 */
public class DeathTile extends AbstractTile {


    /**
     * Constructor for the death tile.
     * It's basically a free tile with a different
     * kind of interaction.
     */
    public DeathTile() {
        super(false);
        images.put("DeathTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/Tile.png"));
        currentImage = images.get("DeathTile");
    }

    /**
     * The death tile, which teleports the player back to their spawn point.
     * @param player The player that interacts with the tile.
     * @return Returns false and doesn't let the player move on to the tile.
     */
    @Override
    public boolean interact(Player player) {
        Position pos = player.getPos();
        pos.setPosition(player.getStartingPos());
        return false;
    }
}
