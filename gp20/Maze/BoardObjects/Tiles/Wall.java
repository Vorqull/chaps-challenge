package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;
import Maze.Position;

public class Wall extends AbstractTile {
    public Wall(Position position) {
        super(position, false);
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