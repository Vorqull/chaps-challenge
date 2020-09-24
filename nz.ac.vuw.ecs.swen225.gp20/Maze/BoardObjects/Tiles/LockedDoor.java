package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;
import Maze.Position;

import java.awt.*;

public class LockedDoor extends AbstractTile {

    private String colour;
    private boolean locked;

    /**
     * .
     * @param position .
     */
    public LockedDoor(Position position, boolean setVertical, String colour) {
        super(position, setVertical);
        this.colour = colour;
        images.put("DoorHorizontalBlue", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalBlue.jpeg"));
        images.put("DoorHorizontalGreen", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalGreen.jpeg"));
        images.put("DoorHorizontalRed", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalRed.jpeg"));
        images.put("DoorHorizontalYellow", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorHorizontalYellow.jpeg"));
        images.put("DoorVerticalBlue", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalBlue.jpeg"));
        images.put("DoorVerticalGreen", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalGreen.jpeg"));
        images.put("DoorVerticalRed", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalRed.jpeg"));
        images.put("DoorVerticalYellow", Toolkit.getDefaultToolkit().getImage("Resources/tiles/DoorVerticalYellow.jpeg"));
        if (rotated){
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
        if(!player.hasKey(colour)) return false;
        return super.interact(player);
    }

    public String getDoorColour() {
        return colour;
    }
}