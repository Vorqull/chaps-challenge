package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The lock to the room containing the exit portal,
 * unlocked when the player collects all of the treasures.
 */
public class ExitLock extends AbstractTile {

    private boolean locked = true;

    /**
     * .
     */
    public ExitLock(boolean setVertical) {
        super(setVertical);
        images.put("ExitLockHorizontal", Toolkit.getDefaultToolkit().getImage("Resources/tiles/ExitLockHorizontal.jpeg"));
        images.put("ExitLockVertical", Toolkit.getDefaultToolkit().getImage("Resources/tiles/ExitLockVertical.jpeg"));
        images.put("FloorTile", Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"));
        if(rotated) currentImage = images.get("ExitLockVertical");
        else currentImage = images.get("ExitLockHorizontal");
    }

    /**
     * Unlocks the door if the player holds the key to this door.
     * @param player The player attempting to unlock the door.
     * @return Returns false because it is locked.
     */
    @Override
    public boolean interact(Player player) {
        return !locked;
    }

    @Override
    public void setChangedTile() {
        super.setChangedTile();
        unlock();
    }

    @Override
    public void unChange() {
        super.unChange();
        locked = true;
        if(rotated) currentImage = images.get("ExitLockVertical");
        else currentImage = images.get("ExitLockHorizontal");
    }

    public void unlock(){
        locked = false;
        currentImage = images.get("FloorTile");
        changed = true;
    }
}
