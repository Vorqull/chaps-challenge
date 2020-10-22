package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;
import Maze.Position;

import java.awt.*;


/**
 * The instant death tile on the board, if the player falls on this tile they respawn to their starting point.
 */
public class DeathTile extends AbstractTile {


    public DeathTile() {
        super(false);
        images.put("DeathTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/Tile.png"));
        currentImage = images.get("DeathTile");
    }

    /**
     * The free tile, which allows the players to be on top of it.
     * @param player The player that interacts with the tile.
     * @return Returns true.
     */
    @Override
    public boolean interact(Player player) {
        Position pos = player.getPos();
        pos.setPosition(player.getStartingPos());
        return false;
    }
}
