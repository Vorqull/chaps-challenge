package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

public class ExitPortal extends AbstractTile {
    public ExitPortal() {
        super(false);
        images.put("Vent", Toolkit.getDefaultToolkit().getImage("Resources/tiles/Vent.jpeg"));
        currentImage = images.get("Vent");
    }

    @Override
    public boolean interact(Player player) {
        return true;
    }

}
