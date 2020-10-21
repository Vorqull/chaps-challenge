package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

public class LockedDoor extends AbstractTile {

    private final String colour;
    private boolean locked = true;


    public LockedDoor(boolean setVertical, String colour) {
        super(setVertical);
        this.colour = colour;
        images.put("DoorHorizontalBlue", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalBlue.jpeg"));
        images.put("DoorHorizontalGreen", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalGreen.jpeg"));
        images.put("DoorHorizontalRed", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalRed.jpeg"));
        images.put("DoorHorizontalYellow", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalYellow.jpeg"));
        images.put("DoorVerticalBlue", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalBlue.jpeg"));
        images.put("DoorVerticalGreen", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalGreen.jpeg"));
        images.put("DoorVerticalRed", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalRed.jpeg"));
        images.put("DoorVerticalYellow", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalYellow.jpeg"));
        images.put("FloorTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"));
        if (rotated) {
            currentImage = images.get("DoorVertical" + colour);
        } else {
            currentImage = images.get("DoorHorizontal" + colour);
        }
    }

    /**
     * Returns true if the player holds the key to this door, otherwise it acts like a wall tile.
     * @param player The player that interacts with the tile.
     * @return Returns true if the player holds the key to this door, otherwise it acts like a wall tile.
     */
    @Override
    public boolean interact(Player player) {
        if(!player.hasKey(colour) && locked){ //If the door's locked and player doesn't have key
            return false;
        } else if(!locked){ //If the door is unlocked
            return true;
        } else { //Unlock the door.
            locked = false;
            currentImage = images.get("FloorTile");
            player.getKeys().remove(player.getKey(colour));
            changed = true;
            return true;
        }
    }

    @Override
    public void setChangedTile() {
        super.setChangedTile();
        locked = false;
        currentImage = images.get("FloorTile");
    }

    public String getDoorColour() {
        return colour;
    }

    public boolean isLocked() {
        return locked;
    }
}
