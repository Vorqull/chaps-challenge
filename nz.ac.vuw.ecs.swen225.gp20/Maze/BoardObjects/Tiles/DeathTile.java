package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;
import Maze.Position;


/**
 * The instant death tile on the board, if the player falls on this tile they respawn to their starting point.
 */
public class DeathTile extends AbstractTile {


    public DeathTile() {
        super(false);
        currentImage = null;
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
