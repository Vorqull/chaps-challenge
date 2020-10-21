package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.AbstractActor;
import Maze.BoardObjects.Actors.Player;
import Maze.Position;

import java.awt.*;

public class Treasure extends AbstractTile {

    private boolean pickedUp = false;

    /**
     * .
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

    public boolean isPickedUp() {
        return pickedUp;
    }
}
