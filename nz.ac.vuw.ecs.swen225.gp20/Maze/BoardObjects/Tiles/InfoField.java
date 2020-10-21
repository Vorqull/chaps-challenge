package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

public class InfoField extends AbstractTile {

    private final String infoText; //Maybe it won't be final

    public InfoField(String infoText) {
        super(false);
        this.infoText = infoText;
        images.put("InfoField", Toolkit.getDefaultToolkit().getImage("Resources/tiles/InfoField.jpeg"));
        currentImage = images.get("InfoField");
    }

    @Override
    public boolean interact(Player player) {
        return true;
    }

    public String getInfoText() {
        return infoText;
    }
}
