package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The treasure tile of the game. ALl treasures tiles must be picked
 * up before the exit locks unlock.
 * @author michael tang 300490290
 */
public class Treasure extends AbstractTile {

    private boolean pickedUp = false;

    /**
     * .The constructor for the treasures.
     */
    public Treasure() {
        super(false);
        images.put("Files", Toolkit.getDefaultToolkit().getImage("Resources/tiles/Files.jpeg"));
        images.put("FloorTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"));
        currentImage = images.get("Files");
    }

    /**
     * The tile interacts with the player. Allowing the player to pick up its treasure
     * and move on top of it.
     * @param player The player.
     * @return Returns true, allowing the player to move on top of it.
     */
    @Override
    public boolean interact(Player player) {
        pickedUp = true;
        player.getTreasures().add(this);
        currentImage = images.get("FloorTile");
        changed = true;
        return true;
    }

    /**
     * A method for setting the treasure to its pick up state.
     * Used for loading save files.
     */
    @Override
    public void setChangedTile() {
        super.setChangedTile();
        pickedUp = true;
        currentImage = images.get("FloorTile");
    }

    @Override
    public void unChange() {
        super.unChange();
        pickedUp = false;
        currentImage = images.get("Files");
    }

    /**
     * Returns whether or not the treasure has been picked up.
     * @return Returns true if the treasure has been picked up.
     */
    public boolean isPickedUp() {
        return pickedUp;
    }
}
