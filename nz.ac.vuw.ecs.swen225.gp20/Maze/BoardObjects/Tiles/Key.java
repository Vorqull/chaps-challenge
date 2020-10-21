package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

public class Key extends AbstractTile {

    private final String colour;
    private boolean pickedUp = false;

    /**
     * .
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

    public String getColour() {
        return colour;
    }
}
