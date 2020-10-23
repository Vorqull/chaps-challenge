package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * THe key tile of the game. Unlocks a locked door
 * of the matching colour.
 * @author michael tang 300490290
 */
public class Key extends AbstractTile {

    private final String colour;
    private boolean pickedUp = false;

    /**
     * .The constructor for the key
     * Initialises the colour of the key
     * @param colour THe colour of the key
     */
    public Key(String colour) {
        super(false);
        this.colour = colour;
        images.put("SwipeCardBlue", Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardBlue.jpeg"));
        images.put("SwipeCardGreen", Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardGreen.jpeg"));
        images.put("SwipeCardRed", Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardRed.jpeg"));
        images.put("SwipeCardYellow", Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardYellow.jpeg"));
        images.put("FloorTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"));
        currentImage = images.get("SwipeCard" + colour);
    }

    /**
     * Works like a free tile. Lets the player pick up the key the
     * first time its being walked over.
     * @param player The player that interacts with the tile.
     * @return Returns true.
     */
    @Override
    public boolean interact(Player player) {
        if(!pickedUp) {
            player.getKeys().add(this);
            pickedUp = true;
            currentImage = images.get("FloorTile");
            changed = true;
        }
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
        currentImage = images.get("SwipeCard" + colour);
    }

    /**
     * Returns the colour of the key
     * @return Returns the colour of the key.
     */
    public String getColour() {
        return colour;
    }
}
