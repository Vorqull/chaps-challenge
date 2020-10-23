package Application;

import Maze.BoardObjects.Actors.Player;
import Maze.BoardObjects.Tiles.Key;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 * Shows the player what keys have been collected in their inventory.
 *
 * @author Iqbal
 */
public class InventoryView extends JPanel {

    /**
     * Given player.
     */
    private Player player;

    /**
     * Size of each item image slot.
     */
    private final int imageSize = 50;

    /**
     * Inventory height.
     */
    private final int inventoryHeight = 2;

    /**
     * Inventory width.
     */
    private final int inventoryWidth = 4;

    /**
     * Constructs a view of the player's inventory.
     * @param player Given player
     */
    public InventoryView(Player player){
        this.player = player;
        this.setPreferredSize(new Dimension(imageSize * inventoryWidth, imageSize * inventoryHeight));
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int gap = (ChapsChallenge.INFO_WIDTH - imageSize * inventoryWidth)/2; //gap before drawing
        int x = gap;
        int y = 0;

        //Draw empty floor tiles as 2x5 "slots"
        for (int row = 0; row < inventoryHeight; row++){
            for (int col = 0; col < inventoryWidth; col++) {
                g.drawImage(Toolkit.getDefaultToolkit().getImage("Resources/tiles/FloorTile.jpeg"), col * imageSize + gap, row * imageSize, imageSize, imageSize, this);
            }
        }

        //Drawing the collected keys
        for (int i = 0; i < player.getKeys().size(); i++){
            Key key = player.getKeys().get(i);
            if (i > inventoryWidth-1) {
                //move down a row if width exceeded
                x = gap;
                y = imageSize;
            }
            switch (key.getColour()){
                case "Blue":
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardBlue.jpeg"), x, y, imageSize, imageSize, this);
                    break;
                case "Red":
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardRed.jpeg"), x, y, imageSize, imageSize, this);
                    break;
                case "Yellow":
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardYellow.jpeg"), x, y, imageSize, imageSize, this);
                    break;
                case "Green":
                    g.drawImage(Toolkit.getDefaultToolkit().getImage("Resources/tiles/SwipeCardGreen.jpeg"), x, y, imageSize, imageSize, this);
                    break;
                default:
                    System.out.println("Inventory view: Key colour not found"); //Something somehow went wrong here
                    break;
            }
            x += imageSize;
        }
    }
}
