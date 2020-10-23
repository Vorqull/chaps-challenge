package Maze.BoardObjects.Tiles;

import Maze.BoardObjects.Actors.Player;

import java.awt.*;

/**
 * The information tile. Basically a free tile that contains a
 * string that informs the player of something about the game.
 * @author michael tang 300490290
 */
public class InfoField extends AbstractTile {

    private final String infoText; //Maybe it won't be final

    /**
     * The constructor of the information tile
     * initialises the information of the tile.
     * @param infoText The information that the level designer
     *                 is telling the player.
     */
    public InfoField(String infoText) {
        super(false);
        this.infoText = infoText;
        images.put("InfoField", Toolkit.getDefaultToolkit().getImage("Resources/tiles/InfoField.jpeg"));
        currentImage = images.get("InfoField");
    }

    /**
     * Acts like a free tile.
     * @param player The player that interacts with the tile.
     * @return Returns true.
     */
    @Override
    public boolean interact(Player player) {
        return true;
    }

    /**
     * Returns the information string of the tile
     * @return Returns the information string of the tile.
     */
    public String getInfoText() {
        return infoText;
    }
}
