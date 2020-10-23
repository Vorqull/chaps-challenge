package Application;

import Persistence.Level;
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
 * Start menu of the game.
 *
 * @author Iqbal
 */
public class StartMenu extends JFrame {

    //window dimensions
    private final int WINDOW_WIDTH = 950;
    private final int WINDOW_HEIGHT = 750;

    protected static int lastLevel = 1; //level 1 by default unless specified
    private Level lastSavedLevel; //last saved level

    private int startMode; // 0 = default, 1 = start from last level, 2 = start from saved level

    /**
     * Constructs the start menu.
     */
    public StartMenu(){
        this.startMode = 0;
        initUI();
        setVisible(true);
    }

    /**
     * Constructs the start menu.
     * When the start button is pressed, the game still start from the last unfinished level.
     *
     * @param lastLevel The last unfinished level
     */
    public StartMenu(int lastLevel){
        this.startMode = 1;
        StartMenu.lastLevel = lastLevel;

        initUI();
        setVisible(true);
    }

    /**
     * Constructs the start menu.
     * When the start button is pressed, the game still start from the given saved level.
     *
     * @param lastSavedLevel Last saved level
     */
    public StartMenu(Level lastSavedLevel){
        this.startMode = 2;
        this.lastSavedLevel = lastSavedLevel;

        initUI();
        setVisible(true);
    }

    /**
     * Initializes window properties and sets the clickable areas.
     */
    public void initUI(){
        setTitle("Chap's Challenge: Among Us Edition");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //Load and add the image to the JFrame
        try {
            BufferedImage image = ImageIO.read(new File("Resources/StartMenu.jpeg"));
            JLabel menuScreen = new JLabel(new ImageIcon(image));
            this.add(menuScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Define the clickable areas on the screen
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                //start
                if (x > 265 && x < 413 && y > 470 && y < 577){
                    //close the start menu and launch the game
                    dispose();
                    switch(startMode){
                        case 0:
                            EventQueue.invokeLater(() -> new ChapsChallenge(1));
                            break;
                        case 1:
                            EventQueue.invokeLater(() -> new ChapsChallenge(lastLevel));
                            break;
                        case 2:
                            EventQueue.invokeLater(() -> new ChapsChallenge(lastSavedLevel));
                            break;
                        default:
                            System.out.println("error start mode = " + startMode);
                            break;
                    }
                }
                //help
                if (x > 543 && x < 690 && y > 467 && y < 577){
                    EventQueue.invokeLater(HelpScreen::new);
                }
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
