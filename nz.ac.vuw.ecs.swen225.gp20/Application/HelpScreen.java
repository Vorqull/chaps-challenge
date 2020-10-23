package Application;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Help screen that shows the player brief rules and the in-game keybindings.
 *
 * @author Iqbal
 */
public class HelpScreen extends JFrame {

    //window dimensions
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 620;

    /**
     * Constructs the help screen.
     */
    public HelpScreen(){
        initUI();
        setVisible(true);
    }

    /**
     * Initializes window properties and sets the clickable areas.
     */
    public void initUI(){
        setTitle("Chap's Challenge Rules");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Load and add the image to the JFrame
        try {
            BufferedImage image = ImageIO.read(new File("Resources/RulesPage.jpeg"));
            JLabel rulesScreen = new JLabel(new ImageIcon(image));
            this.add(rulesScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

}
